/*
 * Copyright 2019 Jeremy Jamet / Kunzisoft.
 *
 * This file is part of KeePassDX.
 *
 *  KeePassDX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  KeePassDX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePassDX.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kunzisoft.keepass.autofill

import android.app.assist.AssistStructure
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


/**
 * Parse AssistStructure and guess username and password fields.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
internal class StructureParser(private val autofillStructure: AssistStructure) {

    val autofillFields = AutofillFieldMetadataCollection()
    var filledAutofillFieldCollection: FilledAutofillFieldCollection = FilledAutofillFieldCollection()
        private set

    fun parseForFill() {
        parse(true)
    }

    fun parseForSave() {
        parse(false)
    }

    /**
     * Traverse AssistStructure and add ViewNode metadata to a flat list.
     */
    private fun parse(forFill: Boolean) {
        Log.d(TAG, "Parsing structure for " + autofillStructure.activityComponent)
        val nodes = autofillStructure.windowNodeCount
        filledAutofillFieldCollection = FilledAutofillFieldCollection()
        for (i in 0 until nodes) {
            parseLocked(forFill, autofillStructure.getWindowNodeAt(i).rootViewNode)
        }
    }

    private fun parseLocked(forFill: Boolean, viewNode: AssistStructure.ViewNode) {
        viewNode.autofillHints?.let { autofillHints ->
            if (autofillHints.isNotEmpty()) {
                if (forFill) {
                    autofillFields.add(AutofillFieldMetadata(viewNode))
                } else {
                    // TODO Autofill parse save
                    //  filledAutofillFieldCollection.add(FilledAutofillField(viewNode))
                }
            }
        }
        val childrenSize = viewNode.childCount
        for (i in 0 until childrenSize) {
            parseLocked(forFill, viewNode.getChildAt(i))
        }
    }

    companion object {
        private val TAG = StructureParser::class.java.name
    }
}
