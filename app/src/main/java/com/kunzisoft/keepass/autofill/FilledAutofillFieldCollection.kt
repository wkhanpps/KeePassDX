/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kunzisoft.keepass.autofill

import android.os.Build
import android.service.autofill.Dataset
import android.util.Log
import android.view.View
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import androidx.annotation.RequiresApi
import com.kunzisoft.keepass.model.EntryInfo
import java.util.HashMap

/**
 * FilledAutofillFieldCollection is the model that represents all of the form data on a client app's page, plus the
 * dataset name associated with it.
 */
@RequiresApi(Build.VERSION_CODES.O)
class FilledAutofillFieldCollection {

    private val hintMap: HashMap<String, String> = HashMap<String, String>()

    /**
     * Add an [entryInfo] to sets values for a list of autofillHints.
     */
    fun add(entryInfo: EntryInfo) {
        entryInfo.username.let {
            if (it.contains("@") && it.isNotEmpty())
                hintMap[View.AUTOFILL_HINT_EMAIL_ADDRESS] = entryInfo.username
            if (it.isNotEmpty())
                hintMap[View.AUTOFILL_HINT_USERNAME] = entryInfo.username
        }
        entryInfo.password.let {
            if (it.isNotEmpty())
                hintMap[View.AUTOFILL_HINT_PASSWORD] = entryInfo.password
        }
    }

    /**
     * Populates a [Dataset.Builder] with appropriate values for each [AutofillId]
     * in a `AutofillFieldMetadataCollection`. In other words, it builds an Autofill dataset
     * by applying saved values (from this `FilledAutofillFieldCollection`) to Views specified
     * in a `AutofillFieldMetadataCollection`, which represents the current page the user is
     * on.
     */
    fun applyToFields(autofillFieldMetadataCollection: AutofillFieldMetadataCollection,
                      datasetBuilder: Dataset.Builder): Boolean {
        var setValueAtLeastOnce = false
        for (hint in autofillFieldMetadataCollection.allAutofillHints) {
            val autofillFields = autofillFieldMetadataCollection.getFieldsForHint(hint) ?: continue
            for (autofillField in autofillFields) {
                val autofillId = autofillField.autofillId
                val autofillType = autofillField.autofillType
                val savedAutofillValue = hintMap[hint]
                autofillId?.let {
                    when (autofillType) {
                        View.AUTOFILL_TYPE_TEXT -> {
                            savedAutofillValue?.let { entryValue ->
                                datasetBuilder.setValue(autofillId, AutofillValue.forText(entryValue))
                                setValueAtLeastOnce = true
                            }
                        }
                        else -> Log.w("FilledAutofillFieldColl", "Invalid autofill type - $autofillType")
                    }
                }
            }
        }
        return setValueAtLeastOnce
    }
}
