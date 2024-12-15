import os
import subprocess
from collections import OrderedDict
import argparse

UNUSED_DEPS_ANCHOR = "Unused dependencies which should be removed:"
DEPENDENCIES_ANCHOR = "dependencies {"
DEPENDENCIES_WHITELIST_ANCHOR = "dependencyAnalysis"
WHITELIST_ANCHOR = "exclude("
JAVA_HEAP_ERROR = "Java heap"
FAILURE = "FAILURE"
FAILED = "FAILED"
ERROR = "ERROR"
LONG_GAP = "\n                "
SHORT_GAP = "\n    "

def show_log(file_name, message):
    print(f"[INFO] {file_name}: {message}")

def show_error(file_name, message):
    print(f"[ERROR] {file_name}: {message}")

def add_dependency_analysis_block(file_path):
    with open(file_path, "r+", encoding="utf-8") as file:
        content = file.read()
        if "dependencyAnalysis" not in content:
            file.write("\ndependencyAnalysis {\n")
            file.write("    val fail = \"fail\"\n")
            file.write("    val ignore = \"ignore\"\n")
            file.write("    issues {\n")
            file.write("        onUnusedDependencies {\n")
            file.write("            severity(fail)\n")
            file.write("            exclude(\n")
            file.write("                \"\",\n")
            file.write("            )\n")
            file.write("        }\n")
            file.write("        onUsedTransitiveDependencies { severity(ignore) }\n")
            file.write("        onIncorrectConfiguration { severity(ignore) }\n")
            file.write("        onCompileOnly { severity(ignore) }\n")
            file.write("        onRuntimeOnly { severity(ignore) }\n")
            file.write("        onUnusedAnnotationProcessors { severity(ignore) }\n")
            file.write("        onRedundantPlugins { severity(ignore) }\n")
            file.write("    }\n")
            file.write("}\n")

def get_errors(task_name):
    subprocess.run(["./gradlew", "--stop"])
    output = subprocess.run(
        ["./gradlew", f"{task_name}:projectHealth", "--parallel"],
        stderr=subprocess.PIPE,
        text=True,
    )
    show_log(task_name, f"Task output:\n {output}\nEnd task output")
    return [line.strip() for line in output.stderr.split("\n") if line.strip()]

def sort_deps(task_name):
    subprocess.run(["./gradlew", f"{task_name}:sortDependencies"])

def get_kts_files():
    # Recursively get all *.kts files in the folder and subfolders
    kts_files = []
    for root, dirs, files in os.walk("."):
        dirs[:] = [d for d in dirs if 'build' not in d]
        for file in files:
            if file.endswith('.kts'):
                kts_files.append(os.path.join(root, file))
    return kts_files

def print_list(file_path, message, list):
    show_log(file_path, message)
    for item in list:
        print(f"{LONG_GAP}{item}")
    print("End of list")

def print_map(file_path, message, map):
    show_log(file_path, message)
    for key, value in map.items():
        print(f"{LONG_GAP}[{key}:{value}]")
    print("End of map")

import subprocess

def get_changed_files():
    try:
        # Run the git diff command to get the list of changed files
        result = subprocess.run(
            ['git', 'diff', '--name-only', 'develop'],
            check=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        changed_files = result.stdout.strip().split('\n')
        changed_files = [file for file in changed_files if file]

        return changed_files

    except subprocess.CalledProcessError as e:
        print(f"An error occurred while running git diff: {e.stderr}")
        return []

def find_gradle_files(changed_files):
    gradle_files = set()

    for file in changed_files:
        # Get the directory of the changed file
        directory = os.path.dirname(file)

        # Traverse up the directory tree to find build.gradle.kts
        while directory:
            gradle_file_path = os.path.join(directory, 'build.gradle.kts')
            if os.path.isfile(gradle_file_path):
                gradle_files.add(gradle_file_path)
                break  # Stop once we find the gradle file in the current directory

            # Move up one directory level
            parent_directory = os.path.dirname(directory)
            if parent_directory == directory:  # If we reach the root directory
                break
            directory = parent_directory

    return list(gradle_files)

def main(arg_file_path, last_handled, remove_bunch, git_diff):
    if arg_file_path:
        kts_files = arg_file_path
    elif git_diff:
        changed_files = get_changed_files()
        kts_files = find_gradle_files(changed_files)
    else:
        kts_files = get_kts_files()
    print_list("", "Files: ", kts_files)
    START_LIB_AMOUNT = 1
    if remove_bunch:
        START_LIB_AMOUNT = remove_bunch
    ignore_paths = [
        "gradle-plugins",
        "buildSrc",
        "settings.gradle.kts",
    ]
    whitelist_paths = [
    ]
    completed_files_counter = 0
    should_skip = False
    if last_handled:
        should_skip = True
    for file_path in reversed(kts_files):
        if any(subpath in file_path for subpath in ignore_paths) and not any(subpath in file_path for subpath in whitelist_paths):
            show_log(file_path, "Skip analysis by path.")
            completed_files_counter += 1
            continue
        task_name = file_path.replace("./", ":").replace("/", ":").replace(":build.gradle.kts", "")
        show_log(file_path, f"Running task...: {task_name}")
        with open(file_path, "r+", encoding="utf-8") as file:
            content = file.read()
        if last_handled and file_path in last_handled:
            should_skip = False
        if should_skip and not any(subpath in file_path for subpath in whitelist_paths):
            show_log(file_path, "Skip analysis by content.")
            completed_files_counter += 1
            continue
        show_log(file_path, f"{len(kts_files) - completed_files_counter} files are left.")
        add_dependency_analysis_block(file_path)
        cur_libs_to_remove_amount = START_LIB_AMOUNT
        prev_removed_index_map = dict()
        last_external_inserted_lib = ''
        first_build = True
        removed_deps = 0
        while True:
            try:
                errors = get_errors(task_name)
                print_list(file_path, "Error list: ", errors)
                if not errors:
                    completed_files_counter += 1
                    show_log(file_path, f"Unused deps were removed: {removed_deps}")
                    break
                should_break = True
                for error in errors:
                    if FAILED in error or FAILURE in error or ERROR in error:
                        should_break = False
                        break
                if should_break:
                    completed_files_counter += 1
                    show_log(file_path, f"Unused deps were removed: {removed_deps}")
                    break
                with open(file_path, "r", encoding="utf-8") as file:
                    content = file.read()
                if UNUSED_DEPS_ANCHOR in errors:
                    last_external_inserted_lib = ''
                    error_block = [error for error in errors if "(" in error and (":" in error or "." in error)]
                    print_list(file_path, "Error dependency block: ", error_block)
                    show_log(file_path, f"Unused deps are left: {len(error_block)}")
                    cur_libs_to_remove_amount = min(max(1, cur_libs_to_remove_amount), len(error_block))
                    prev_removed_index_map.clear()
                    if first_build:
                        removed_deps = len(error_block)
                        first_build = False
                    sub_list = error_block[:cur_libs_to_remove_amount]
                    for dep in sub_list:
                        index = content.find(dep)
                        if index == -1:
                            cur_libs_to_remove_amount -= 1
                            removed_deps -= 1
                            start = dep.find('(')
                            end = dep.find(')')
                            if start != -1 and end != -1 and start < end:
                                last_external_inserted_lib = dep[start+1:end]
                                show_log(file_path, f"External lib {last_external_inserted_lib} inserted.")
                                white_list_lib = f'{LONG_GAP}{last_external_inserted_lib}.map {{ it.name() }}.get(),'
                                if white_list_lib in content:
                                    show_error(file_path, f"Lib {last_external_inserted_lib} already contains in whitelist. Need manual support")
                                    return
                                content = content[:content.rfind(WHITELIST_ANCHOR) + len(WHITELIST_ANCHOR)] + white_list_lib + content[content.rfind(WHITELIST_ANCHOR) + len(WHITELIST_ANCHOR):]
                            else:
                                show_error(file_path, f"Parsing error for non presented lib: {lib_to_insert}")
                                return
                    if last_external_inserted_lib:
                        show_log(file_path, f"Not presented external lib found: {last_external_inserted_lib}")
                    else:
                        for dep in sub_list:
                            index = content.find(dep)
                            if index != -1:
                                prev_removed_index_map[index] = dep
                        sorted_dict = OrderedDict(sorted(prev_removed_index_map.items(), reverse=True))
                        print_map(file_path, f"Candidates for removing: ", sorted_dict)
                        if len(prev_removed_index_map) != cur_libs_to_remove_amount:
                            show_error(file_path, f"Removed lib map size: {len(prev_removed_index_map)} != lib amount: {cur_libs_to_remove_amount}")
                            return
                        remove_block = content
                        for index, dep in sorted_dict.items():
                            if index == -1:
                                show_error(file_path, f"Can't find entry {dep} with index {index}")
                                return
                            else:
                                remove_block = remove_block[:index] + remove_block[index + len(dep):]
                        content = remove_block
                elif any(JAVA_HEAP_ERROR in error for error in errors):
                    show_log(file_path, f"Java heap error. Continue scanning...")
                    continue
                else:
                    if first_build:
                        show_error(file_path, f"First build failed. Try to run {task_name}:projectHealth, fix build issues and rerun this python script")
                        return
                    show_log(file_path, f"Error handling...")
                    if last_external_inserted_lib:
                        show_log(file_path, f"Replace wrong whitelist lib: {last_external_inserted_lib}")
                        white_list_lib_old = f'{LONG_GAP}{last_external_inserted_lib}.map {{ it.name() }}.get(),'
                        white_list_lib_new = f'{LONG_GAP}{last_external_inserted_lib}.asProvider().map {{ it.name() }}.get(),'
                        if white_list_lib_new in content:
                            show_error(file_path, f"Lib {last_external_inserted_lib} already contains in whitelist. Need manual support")
                            return
                        last_external_inserted_lib = ''
                        content = content.replace(white_list_lib_old, white_list_lib_new)
                    else:
                        print_map(file_path, "Try to handle libs: ", prev_removed_index_map)
                        if len(prev_removed_index_map) != cur_libs_to_remove_amount:
                            show_error(file_path, f"Removed lib map size: {len(prev_removed_index_map)} != lib amount: {cur_libs_to_remove_amount}")
                            return

                        insert_index = content.find(DEPENDENCIES_ANCHOR) + len(DEPENDENCIES_ANCHOR)
                        if cur_libs_to_remove_amount > 1:
                            cur_libs_to_remove_amount //= 2
                            show_log(file_path, f"Decrease lib amount by 2. Current remove block is: {cur_libs_to_remove_amount}")
                            insert_block = SHORT_GAP.join(dep for dep in prev_removed_index_map.values())
                            content = content[:insert_index] + SHORT_GAP + insert_block + content[insert_index:]
                        else:
                            if len(prev_removed_index_map) != 1:
                                show_error(file_path, f"Expected list lib size is 1 but actual is: {prev_removed_index_map}")
                                return
                            removed_deps -= 1
                            lib_to_insert = next(iter(prev_removed_index_map.values()))
                            content = content[:insert_index] + f"{SHORT_GAP}{lib_to_insert}" + content[insert_index:]
                            white_list_lib = ''
                            if '.' in lib_to_insert:
                                start = lib_to_insert.find('(')
                                end = lib_to_insert.find(')')
                                if start != -1 and end != -1 and start < end:
                                    last_external_inserted_lib = lib_to_insert[start+1:end]
                                    show_log(file_path, f"External lib {last_external_inserted_lib} inserted after error.")
                                    white_list_lib = f'{LONG_GAP}{last_external_inserted_lib}.map {{ it.name() }}.get(),'
                                else:
                                    show_error(file_path, f"Parsing error for lib: {lib_to_insert}")
                                    return
                            else:
                                extracted_lib = lib_to_insert.split('"')[1]
                                white_list_lib = f'{LONG_GAP}"{extracted_lib}",'
                            show_log(file_path, f"Try to add lib to whitelist: {white_list_lib}")
                            if white_list_lib in content:
                                show_error(file_path, f"Lib {lib_to_insert} already contains in whitelist.")
                                return
                            content = content[:content.rfind(WHITELIST_ANCHOR) + len(WHITELIST_ANCHOR)] + white_list_lib + content[content.rfind(WHITELIST_ANCHOR) + len(WHITELIST_ANCHOR):]
                            cur_libs_to_remove_amount = START_LIB_AMOUNT

                with open(file_path, "w", encoding="utf-8") as file:
                    file.write(content)
            except Exception as ex:
                show_log(file_path, f"Exception: {ex}")

        sort_deps(task_name)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Run UnusedDepsTask.")
    parser.add_argument('--file_path', type=str, nargs='+', help='Path to the build.gradle.kts file.')
    parser.add_argument('--last_handled', type=str, help='Start analysis from the latest handled gradle file.')
    parser.add_argument('--remove_bunch', type=str, help='Size of bunch of libs that script will try to remove.')
    parser.add_argument('--git_diff', action='store_true', help='Remove unused deps for all changed modules compared to develop branch')
    args = parser.parse_args()
    main(args.file_path, args.last_handled, args.remove_bunch, args.git_diff)
