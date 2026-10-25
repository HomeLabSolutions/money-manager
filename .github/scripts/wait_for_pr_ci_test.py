import unittest
from unittest.mock import patch

from wait_for_pr_ci import wait_for_ci


def status(event="pull_request", state="completed", conclusion="success", sha="expected"):
    return {"event": event, "status": state, "conclusion": conclusion, "headSha": sha, "url": "https://example.invalid/ci"}


class WaitForPrCiTest(unittest.TestCase):
    def setUp(self):
        printer = patch("builtins.print")
        self.printer = printer.start()
        self.addCleanup(printer.stop)

    @patch("wait_for_pr_ci.run_status", return_value=status())
    def test_accepts_successful_pr_check(self, _):
        wait_for_ci(123, "expected")

    @patch("wait_for_pr_ci.run_status", return_value=status(event="workflow_dispatch"))
    def test_manual_success_does_not_satisfy_required_pr_checks(self, _):
        with self.assertRaisesRegex(ValueError, "workflow_dispatch does not count"):
            wait_for_ci(123, "expected")

    @patch("wait_for_pr_ci.run_status", return_value=status(sha="different"))
    def test_rejects_check_for_another_commit(self, _):
        with self.assertRaisesRegex(ValueError, "another commit"):
            wait_for_ci(123, "expected")

    @patch("wait_for_pr_ci.time.sleep")
    @patch("wait_for_pr_ci.run_status")
    def test_waits_for_approval_and_then_real_ci_completion(self, run, sleep):
        run.side_effect = [status(conclusion="action_required"), status(state="queued", conclusion=""),
                           status(state="in_progress", conclusion=""), status()]
        wait_for_ci(123, "expected")
        self.assertEqual(4, run.call_count)
        self.assertEqual(3, sleep.call_count)
        self.assertIn("Approve workflows", self.printer.call_args_list[0].args[0])

    @patch("wait_for_pr_ci.run_status")
    def test_ci_failure_cancellation_and_timeout_block_merge(self, run):
        for conclusion in ["failure", "cancelled", "timed_out"]:
            with self.subTest(conclusion=conclusion):
                run.return_value = status(conclusion=conclusion)
                with self.assertRaisesRegex(ValueError, conclusion):
                    wait_for_ci(123, "expected")

    @patch("wait_for_pr_ci.time.sleep")
    @patch("wait_for_pr_ci.time.monotonic", side_effect=[0, 0, 1501])
    @patch("wait_for_pr_ci.run_status", return_value=status(conclusion="action_required"))
    def test_approval_wait_has_a_deadline(self, _, __, ___):
        with self.assertRaisesRegex(ValueError, "Timed out"):
            wait_for_ci(123, "expected")


if __name__ == "__main__":
    unittest.main()
