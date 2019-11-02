package com.pbasket.yellow.basketball;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.util.Log;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Nikitos on 2/10/14.
 */
class GameHelperUtils {
    public static final int R_UNKNOWN_ERROR = 0;
    public static final int R_SIGN_IN_FAILED = 1;
    public static final int R_APP_MISCONFIGURED = 2;
    public static final int R_LICENSE_FAILED = 3;

    private final static String[] FALLBACK_STRINGS = {
            "*Unknown error.",
            "*Failed to sign in. Please check your network connection and try again.",
            "*The application is incorrectly configured. Check that the package name and signing certificate match the client ID created in Developer Console. Also, if the application is not yet published, check that the account you are trying to sign in with is listed as a tester account. See logs for more information.",
            "*License check failed."
    };


    static void printMisconfiguredDebugInfo(Context ctx) {
        Log.w("GameHelper", "****");
        Log.w("GameHelper", "****");
        Log.w("GameHelper", "**** APP NOT CORRECTLY CONFIGURED TO USE GOOGLE PLAY GAME SERVICES");
        Log.w("GameHelper", "**** This is usually caused by one of these reasons:");
        Log.w("GameHelper", "**** (1) Your package name and certificate fingerprint do not match");
        Log.w("GameHelper", "****     the client ID you registered in Developer Console.");
        Log.w("GameHelper", "**** (2) Your App ID was incorrectly entered.");
        Log.w("GameHelper", "**** (3) Your game settings have not been published and you are ");
        Log.w("GameHelper", "****     trying to log in with an account that is not listed as");
        Log.w("GameHelper", "****     a test account.");
        Log.w("GameHelper", "****");
        if (ctx == null) {
            Log.w("GameHelper", "*** (no Context, so can't print more debug info)");
            return;
        }

        Log.w("GameHelper", "**** To help you debug, here is the information about this app");
        Log.w("GameHelper", "**** Package name         : " + ctx.getPackageName());
        Log.w("GameHelper", "**** Cert SHA1 fingerprint: " + getSHA1CertFingerprint(ctx));
        Log.w("GameHelper", "**** App ID from          : " + getAppIdFromResource(ctx));
        Log.w("GameHelper", "****");
        Log.w("GameHelper", "**** Check that the above information matches your setup in ");
        Log.w("GameHelper", "**** Developer Console. Also, check that you're logging in with the");
        Log.w("GameHelper", "**** right account (it should be listed in the Testers section if");
        Log.w("GameHelper", "**** your project is not yet published).");
        Log.w("GameHelper", "****");
        Log.w("GameHelper", "**** For more information, refer to the troubleshooting guide:");
        Log.w("GameHelper", "****   http://developers.google.com/games/services/android/troubleshooting");
    }

    static String getAppIdFromResource(Context ctx) {
        try {
            Resources res = ctx.getResources();
            String pkgName = ctx.getPackageName();
            int res_id = res.getIdentifier("app_id", "string", pkgName);
            return res.getString(res_id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "??? (failed to retrieve APP ID)";
        }
    }

    static String getSHA1CertFingerprint(Context ctx) {
        try {
            Signature[] sigs = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            if (sigs.length == 0) {
                return "ERROR: NO SIGNATURE.";
            } else if (sigs.length > 1) {
                return "ERROR: MULTIPLE SIGNATURES";
            }
            byte[] digest = MessageDigest.getInstance("SHA1").digest(sigs[0].toByteArray());
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < digest.length; ++i) {
                if (i > 0) {
                    hexString.append(":");
                }
                byteToString(hexString, digest[i]);
            }
            return hexString.toString();

        } catch (PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
            return "(ERROR: package not found)";
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return "(ERROR: SHA1 algorithm not found)";
        }
    }

    static void byteToString(StringBuilder sb, byte b) {
        int unsigned_byte = b < 0 ? b + 256 : b;
        int hi = unsigned_byte / 16;
        int lo = unsigned_byte % 16;
        sb.append("0123456789ABCDEF".substring(hi, hi + 1));
        sb.append("0123456789ABCDEF".substring(lo, lo + 1));
    }
}
