/*
 * Copyright 2017 Brian Pellin, Jeremy Jamet / Kunzisoft.
 *     
 * This file is part of KeePass DX.
 *
 *  KeePass DX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  KeePass DX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePass DX.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.keepassdroid.tests;

import java.util.UUID;

import junit.framework.TestCase;

import com.keepassdroid.database.PwEntryV4;
import com.keepassdroid.database.PwGroupV4;
import com.keepassdroid.database.PwIconCustom;
import com.keepassdroid.database.PwIconStandard;
import com.keepassdroid.database.security.ProtectedBinary;
import com.keepassdroid.database.security.ProtectedString;

public class PwEntryTestV4 extends TestCase {
	public void testAssign() {
		PwEntryV4 entry = new PwEntryV4();
		
		entry.setAdditional("test223");
		
		entry.setAutoType(entry.new AutoType());
		entry.getAutoType().defaultSequence = "1324";
		entry.getAutoType().enabled = true;
		entry.getAutoType().obfuscationOptions = 123412432109L;
		entry.getAutoType().put("key", "value");
		
		entry.setBackgroupColor("blue");
		entry.putProtectedBinary("key1", new ProtectedBinary(false, new byte[] {0,1}));
		entry.setCustomIcon(new PwIconCustom(UUID.randomUUID(), new byte[0]));
		entry.setForegroundColor("red");
		entry.addToHistory(new PwEntryV4());
		entry.setIcon(new PwIconStandard(5));
		entry.setOverrideURL("override");
		entry.setParent(new PwGroupV4());
		entry.addField("key2", new ProtectedString(false, "value2"));
		entry.setUrl("http://localhost");
		entry.setUUID(UUID.randomUUID());

		PwEntryV4 target = new PwEntryV4();
		target.assign(entry);
		
		/* This test is not so useful now that I am not implementing value equality for Entries
		assertTrue("Entries do not match.", entry.equals(target));
		*/
		
	}

}
