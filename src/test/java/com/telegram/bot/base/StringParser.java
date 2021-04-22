/*
 *
 * Copyright © 2020 EPAM Systems, Inc. All Rights Reserved. All information contained herein is, and remains the
 * property of EPAM Systems, Inc. and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from EPAM Systems, Inc
 */

package com.telegram.bot.base;

import java.util.LinkedList;
import java.util.List;

public class StringParser {
    public static List<String> parse(String str, String regDivider) {
        String[] elements = str.split(regDivider);
        List<String> result = new LinkedList<>();
        for(String element: elements) {
            String tmp = element.trim();
            //if (tmp.length() > 0)
                result.add(regDivider + tmp);
        }
        return result;
    }
}
