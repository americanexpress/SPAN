package com.americanexpress.span.utility;

import com.americanexpress.span.constants.SPANConstants;

/***
 *  This interface is used to assign SPAN Configuration file name and app profile environment variable.
 *
 */

public interface PropertyConfiguration {

    //User can override default method if the file name is predefined.
    default String getSPANConfigFileName() {
        return null;
    }

    //User can override default method if the app profile environment variable is predefined.
    default String getAppProfileEnvVariable(){
        return SPANConstants.APP_PROFILE;
    }
}
