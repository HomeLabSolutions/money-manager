import base64
import json
import unittest
from unittest.mock import call, patch
from urllib.parse import parse_qs

from check_play_version import PLAY_URL, TOKEN_URL, access_token, check_available


class CheckPlayVersionTest(unittest.TestCase):
    def responses(self):
        return [
            {"id": "verification"},
            {"bundles": [{"versionCode": 1403001}]},
            {"apks": [{"versionCode": 1309001}]},
            {"tracks": [{"track": "internal", "releases": [{"versionCodes": ["1403002"]}]}]},
            {},
        ]

    @patch("check_play_version.request_json")
    def test_new_code_is_accepted_and_edit_is_deleted_without_committing(self, request):
        request.side_effect = self.responses()
        self.assertEqual(1403002, check_available("test-token", 1403003))
        self.assertEqual(call(f"{PLAY_URL}/verification", "DELETE", token="test-token"), request.call_args)
        self.assertFalse(any("commit" in args[0] for args, _ in request.call_args_list))

    @patch("check_play_version.request_json")
    def test_existing_or_lower_code_is_rejected_and_edit_is_deleted(self, request):
        for code in [1403002, 1403001]:
            with self.subTest(code=code):
                request.reset_mock(side_effect=True)
                request.side_effect = self.responses()
                with self.assertRaisesRegex(ValueError, "highest uploaded code 1403002"):
                    check_available("test-token", code)
                self.assertEqual(call(f"{PLAY_URL}/verification", "DELETE", token="test-token"), request.call_args)

    @patch("check_play_version.request_json")
    def test_uploaded_bundle_outside_active_tracks_also_reserves_code(self, request):
        responses = self.responses()
        responses[1] = {"bundles": [{"versionCode": 1500000}]}
        request.side_effect = responses
        with self.assertRaisesRegex(ValueError, "highest uploaded code 1500000"):
            check_available("test-token", 1403003)

    @patch("check_play_version.request_json")
    def test_empty_app_accepts_first_code(self, request):
        request.side_effect = [{"id": "verification"}, {}, {}, {}, {}]
        self.assertEqual(0, check_available("test-token", 1))

    @patch("check_play_version.request_json")
    def test_read_failure_still_deletes_temporary_edit(self, request):
        request.side_effect = [{"id": "verification"}, OSError("temporary API failure"), {}]
        with self.assertRaisesRegex(OSError, "temporary API failure"):
            check_available("test-token", 1403003)
        self.assertEqual(call(f"{PLAY_URL}/verification", "DELETE", token="test-token"), request.call_args)

    @patch("check_play_version.request_json")
    @patch("check_play_version.sign_assertion", return_value=b"test-signature")
    @patch("check_play_version.time.time", return_value=1000)
    def test_authentication_uses_android_publisher_scope_and_short_lived_signed_jwt(self, _, sign, request):
        credentials = {"type": "service_account", "client_email": "test@example.invalid", "private_key": "test-only"}
        request.return_value = {"access_token": "test-token"}
        self.assertEqual("test-token", access_token(credentials))
        unsigned = sign.call_args.args[0]
        header, claims = [json.loads(base64.urlsafe_b64decode(part + b"=" * (-len(part) % 4))) for part in unsigned.split(b".")]
        self.assertEqual({"alg": "RS256", "typ": "JWT"}, header)
        self.assertEqual("test@example.invalid", claims["iss"])
        self.assertEqual("https://www.googleapis.com/auth/androidpublisher", claims["scope"])
        self.assertEqual(TOKEN_URL, claims["aud"])
        self.assertEqual(3600, claims["exp"] - claims["iat"])
        form = parse_qs(request.call_args.args[2].decode())
        self.assertEqual(["urn:ietf:params:oauth:grant-type:jwt-bearer"], form["grant_type"])
        self.assertEqual(3, len(form["assertion"][0].split(".")))

    @patch("check_play_version.sign_assertion")
    def test_rejects_wrong_credentials_type_before_signing(self, sign):
        with self.assertRaisesRegex(ValueError, "service account JSON"):
            access_token({"type": "authorized_user"})
        sign.assert_not_called()


if __name__ == "__main__":
    unittest.main()
