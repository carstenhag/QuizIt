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

        int title;
        int message;

        switch (dialogType) {
            case GENERIC_ERR: {
                title = R.string.error_generic_title;
                message = R.string.error_generic_message;
                break;
            }
            case NO_FRIENDS_ERR: {
                title = R.string.error_generic_title;
                message = R.string.error_no_friends_added;
                break;
            }
            case NETWORK_ERR: {
                title = R.string.error_generic_title;
                message = R.string.error_network_unavailable;
                break;
            }
            case NETWORK_UNAVAIL_ERR: {
                title = R.string.error_generic_title;
                message = R.string.error_network_unavailable;
                break;
            }
            case INPUT_ERR: {
                title = R.string.error_invalid_input_title;
                message = R.string.error_invalid_input_generic;
                break;
            }
            case INPUT_USERNAME_ERR: {
                title = R.string.error_invalid_input_title;
                message = R.string.error_invalid_input_username;
                break;
            }
            case INPUT_PW_ERR: {
                title = R.string.error_invalid_input_title;
                message = R.string.error_invalid_input_password;
                break;
            }
            case INPUT_EMAIL_ERR: {
                title = R.string.error_invalid_input_title;
                message = R.string.error_invalid_input_email;
                break;
            }
            case CREATING_USER_ERR: {
                title = R.string.error_generic_title;
                message = R.string.error_creating_user;
                break;
            }
            case LOGIN_ERR: {
                title = R.string.error_generic_title;
                message = R.string.error_generic_message;
                break;
            }
            default: {
                title = R.string.error_generic_title;
                message = R.string.error_generic_message;
            }
            // more cases as the need for them comes
        }
        return new MaterialDialogBuilder(context, context.getString(title), context.getString(message));
    }


    // Used to specify all possible Strings
    public static MaterialDialogBuilder buildSpecific(Context context, String title, String content, String positiveText, MaterialDialog.ButtonCallback buttonCallback) {
        return new MaterialDialogBuilder(context, title, content, positiveText, buttonCallback);
    }

    // Used to specify title and content Strings
    private MaterialDialogBuilder(Context context, String title, String content) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(context.getString(android.R.string.ok))
                .show();
    }


    // Creates the Dialog with specific string and callback
    private MaterialDialogBuilder(Context context, String title, String content, String positiveText, MaterialDialog.ButtonCallback buttonCallback) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .callback(buttonCallback)
                .show();
    }

}
