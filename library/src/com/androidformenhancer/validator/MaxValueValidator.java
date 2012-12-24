/*
 * Copyright 2012 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidformenhancer.validator;

import com.androidformenhancer.R;
import com.androidformenhancer.annotation.MaxValue;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Validates that the value exceeds the max value or not.
 * 
 * @author Soichiro Kashima
 */
public class MaxValueValidator extends Validator {

    private static final String TAG = "MaxValueValidator";

    @Override
    public String validate(final Field field) {
        Object value;
        try {
            value = field.get(getTarget());
        } catch (Exception e) {
            // TODO Throw some exception to inform caller this illegal state
            Log.v(TAG, e.getMessage());
            return null;
        }

        MaxValue maxValue = field.getAnnotation(MaxValue.class);
        if (maxValue != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                final String strValue = (String) value;
                if (TextUtils.isEmpty(strValue)) {
                    return null;
                }
                int nValue = -1;
                boolean hasError = false;
                try {
                    nValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    hasError = true;
                }
                if (hasError || maxValue.value() < nValue) {
                    String name = field.getName();
                    int nameResId = getNameResourceId(field);
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    nameResId = maxValue.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    Object[] messageParams = new Object[] {
                            name, maxValue.value()
                    };
                    return getMessage(R.styleable.ValidatorMessages_afeErrorMaxValue,
                            R.string.afe__msg_validation_max_value,
                            messageParams);
                }
            }
        }

        return null;
    }

}
