from pathlib import Path
import tempfile
import unittest

from release_version import parse_version, update_version


BUILD_FILE = '''// Project configuration
extra["versionMajor"] = 1
extra["versionMinor"] = 4
extra["versionPatch"] = 3
extra["versionBuild"] = 1

// The remaining build configuration stays unchanged.
'''


class ReleaseVersionTest(unittest.TestCase):
    def test_existing_version_code_formula(self):
        self.assertEqual(1403003, parse_version("1.4.3.3").code)
        self.assertEqual("1.4.3.3", parse_version("1.4.3.3").name)

    def test_version_code_rollovers_do_not_collide(self):
        for previous, following in [
            ("1.4.3.999", "1.4.4.0"),
            ("1.4.99.999", "1.5.0.0"),
            ("1.9.99.999", "2.0.0.0"),
        ]:
            with self.subTest(previous=previous, following=following):
                self.assertEqual(parse_version(previous).code + 1, parse_version(following).code)

    def test_google_play_code_boundaries(self):
        self.assertEqual(1, parse_version("0.0.0.1").code)
        self.assertEqual(2_100_000_000, parse_version("2100.0.0.0").code)

    def test_rejects_malformed_ambiguous_or_out_of_range_versions(self):
        for value in [
            "", "1.4.3", "1.4.3.3.1", "1.04.3.3", "1.4.03.3", "1.4.3.-1", "1.4.3.1000",
            "1.4.100.0", "1.10.0.0", "2100.0.0.1", "9999.0.0.0", "0.0.0.0", "1.4.3.3\n",
            "1.4.3.3; exit 0", "$(id)", " 1.4.3.3", "1.4.3.3 ",
        ]:
            with self.subTest(value=value), self.assertRaises(ValueError):
                parse_version(value)

    def test_updates_all_four_fields_and_preserves_other_configuration(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "build.gradle.kts"
            path.write_text(BUILD_FILE)
            self.assertTrue(update_version(path, parse_version("2.5.6.7")))
            expected = BUILD_FILE.replace('versionMajor"] = 1', 'versionMajor"] = 2')
            expected = expected.replace('versionMinor"] = 4', 'versionMinor"] = 5')
            expected = expected.replace('versionPatch"] = 3', 'versionPatch"] = 6')
            expected = expected.replace('versionBuild"] = 1', 'versionBuild"] = 7')
            self.assertEqual(expected, path.read_text())

    def test_reusing_unpublished_repository_version_needs_no_commit(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "build.gradle.kts"
            path.write_text(BUILD_FILE)
            self.assertFalse(update_version(path, parse_version("1.4.3.1")))
            self.assertEqual(BUILD_FILE, path.read_text())

    def test_downgrade_does_not_change_file(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "build.gradle.kts"
            path.write_text(BUILD_FILE)
            with self.assertRaises(ValueError):
                update_version(path, parse_version("1.4.2.999"))
            self.assertEqual(BUILD_FILE, path.read_text())

    def test_missing_or_duplicate_field_does_not_partially_update_file(self):
        for text in [
            BUILD_FILE.replace('extra["versionBuild"] = 1\n', ""),
            BUILD_FILE + 'extra["versionMajor"] = 1\n',
        ]:
            with self.subTest(text=text), tempfile.TemporaryDirectory() as directory:
                path = Path(directory) / "build.gradle.kts"
                path.write_text(text)
                with self.assertRaises(ValueError):
                    update_version(path, parse_version("2.0.0.0"))
                self.assertEqual(text, path.read_text())


if __name__ == "__main__":
    unittest.main()
