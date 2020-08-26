package com.inderproduction.theinderapp.Utilities;

import android.widget.EditText;

public class Validations {

    public static boolean isUsernameValid(EditText editText){
        String input = editText.getText().toString().trim();
        if(input.equals("")){
            editText.setError("Username Cannot be Empty");
            return false;
        } else if(input.contains(" ")){
            editText.setError("Username Cannot Contain Space");
            return false;
        } else if(input.length() <4 ){
            editText.setError("Username must be between 4 and 12");
            return false;
        }
        editText.setError(null);
        return true;
    }


    public static boolean isEmailValid(EditText editText){
        String input = editText.getText().toString().trim();
        if(input.equals("")){
            editText.setError("Email Cannot be Empty");
            return false;
        } else if(input.contains(" ")){
            editText.setError("Email Cannot Contain Space");
            return false;
        } else if(!input.matches(
                "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            editText.setError("Not a valid email");
            return false;
        }
        return true;

    }

    public static boolean isNumberValid(EditText editText){
        String input = editText.getText().toString().trim();
        if(input.equals("")){
            editText.setError("Number Cannot be Empty");
            return false;
        } else if(input.contains(" ")){
            editText.setError("Number Cannot Contain Space");
            return false;
        } else if(input.length() != 10){
            editText.setError("Invalid Number Format");
            return false;
        }
        return true;
    }

    public static boolean isGenericallyValid(EditText editText){
        String input = editText.getText().toString().trim();
        if(input.equals("")){
            editText.setError("Field Cannot be Empty");
            return false;
        }
        return true;
    }





    /* Explanations:

            (?=.*[0-9]) a digit must occur at least once
            (?=.*[a-z]) a lower case letter must occur at least once
            (?=.*[A-Z]) an upper case letter must occur at least once
            (?=.*[@#$%^&+=]) a special character must occur at least once
            (?=\\S+$) no whitespace allowed in the entire string
            .{8,} at least 8 characters */

    public static boolean isPasswordValid(EditText editText){
        String input = editText.getText().toString().trim();

        String regexPass = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";


        if(input.equals("")){
            editText.setError("Password Cannot be Empty");
            return false;
        } else if(input.contains(" ")){
            editText.setError("Password Cannot Contain Space");
            return false;
        } else if(!input.matches(regexPass)) {
            editText.setError("Not a valid password");
            return false;
        }
        return true;

    }


}


