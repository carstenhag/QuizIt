package pw.moter8.quizit;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

public class MaterialDialogBuilder {

    public static enum DialogType {
        GENERIC_ERR,
        NO_FRIENDS_ERR,
        NETWORK_ERR,
        INPUT_ERR,
        INPUT_USERNAME_ERR,
        INPUT_PW_ERR,
        INPUT_EMAIL_ERR,
        CREATING_USER_ERR,
        LOGIN_ERR,
        NETWORK_UNAVAIL_ERR,
        POST_SUCCESS,
    }


    public static MaterialDialogBuilder build(Context context, DialogType dialogType) {
        switch (dialogType) {
            case GENERIC_ERR: {
                return new MaterialDialogBuilder(context,
                        context.getString(R.string.error_generic_title),
                        context.getString(R.string.error_generic_message));
            }
            case NO_FRIENDS_ERR: {
                return new MaterialDialogBuilder(context,
                        context.getString(R.string.error_generic_title),
                        context.getString(R.string.error_no_friends_added));
            }
            case NETWORK_ERR: {
                return new MaterialDialogBuilder(context,
                        context.getString(R.string.error_generic_title),
                        context.getString(R.string.error_network_unavailable));
            }
            case NETWORK_UNAVAIL_ERR: {
                return new MaterialDialogBuilder(context,
                        context.getString(R.string.error_generic_title),
                        context.getString(R.string.error_network_unavailable));
            }
            case INPUT_ERR: {
                return new MaterialDialogBuilder(context,
                        context.getString(R.string.error_invalid_input_title),
                        context.getString(R.string.error_invalid_input_generic));
            }
            case INPUT_USERNAME_ERR: {
                return new MaterialDialogBuilder(context,
                        context.getString(R.string.error_invalid_input_title),
                        context.getString(R.string.error_invalid_input_username));
            }
            case INPUT_PW_ERR: {
                return new MaterialDialogBuilder(context,
                        context.getString(R.string.error_invalid_input_title),
                        context.getString(R.string.error_invalid_input_password));
            }
            case INPUT_EMAIL_ERR: {
                return new MaterialDialogBuilder(context,
                        context.getString(R.string.error_invalid_input_title),
                        context.getString(R.string.error_invalid_input_email));
            }
            case CREATING_USER_ERR: {
                return new MaterialDialogBuilder(context,
                        context.getString(R.string.error_generic_title),
                        context.getString(R.string.error_creating_user));
            }
            case LOGIN_ERR: {
                return new MaterialDialogBuilder(context,
                        context.getString(R.string.error_generic_title),
                        context.getString(R.string.error_generic_message));
            }
            // more cases as the need for them comes

        }
        return new MaterialDialogBuilder(context,
                "Easter Egg",
                "You discovered a Secret! May the Dank be with you.");
    }


    // Used to specify all possible Strings
    public static MaterialDialogBuilder buildSpecific(Context context, String title, String content, String positiveText) {
        return new MaterialDialogBuilder(context, title, content, positiveText);
    }

    // Used to specify title and content Strings
    private MaterialDialogBuilder(Context context, String title, String content) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(context.getString(android.R.string.ok))
                .show();
    }


    // Creates the Dialog
    private MaterialDialogBuilder(Context context, String title, String content, String positiveText) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .show();
    }

}
