package controllerLogic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerLogic {

    public static boolean isValidEmail( String email ) {
        Pattern pattern = Pattern.compile( "^[a-zA-Z0-9_+&*-]+(?:\\" +
                ".[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\" +
                ".)+[a-zA-Z]{2,7}$" );
        return pattern.matcher( email ).matches();
    }

    public static boolean isValidMileThreshold( String mile ) {
        Pattern pattern = Pattern.compile( "^\\d+(?:\\.\\d{0,2})?$" );
        return pattern.matcher( mile ).matches();
    }

    public static boolean isValidPhoneNumber( String phoneNumber ) {
        Pattern pattern = Pattern.compile( "\\d{3}-\\d{3}-\\d{4}" );
        Matcher matcher = pattern.matcher( phoneNumber );
        return ( matcher.find() && matcher.group().equals( phoneNumber ) );
    }

    public static boolean isValidCarrier( String carrier ) {
        switch ( carrier ) {
            case "AT&T" , "Sprint" , "Verizon" , "T-Mobile" -> { return true; }
            default -> { return false; }
        }
    }

    public static boolean isValid_MileInput(String mile) {
        Pattern pattern = Pattern.compile("^[0-9]*(\\.)?[0-9]+$");
        Matcher matcher = pattern.matcher(mile);
        if (mile.equals( "0" )) return false;
        return (matcher.find() && matcher.group().equals(mile));
    }
}