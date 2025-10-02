package com.nine.travelerscompass.compat;

import com.nine.travelerscompass.config.TCConfig;

public enum NEI {

    NONE,
    REI,
    EMI,
    JEI;

    public boolean allowed(){
        return switch (this){
            case JEI -> TCConfig.JEI_COMPATIBILITY.get();
            case EMI -> TCConfig.EMI_COMPATIBILITY.get();
            case REI -> TCConfig.REI_COMPATIBILITY.get();
            default -> true;
        };
    }

}
