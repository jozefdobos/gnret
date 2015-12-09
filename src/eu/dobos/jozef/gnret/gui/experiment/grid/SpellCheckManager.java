/**
*  Copyright (C) 2011 Jozef Dobos
*
*  This program is free software: you can redistribute it and/or modify
*  it under the terms of the GNU Affero General Public License as
*  published by the Free Software Foundation, either version 3 of the
*  License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU Affero General Public License for more details.
*
*  You should have received a copy of the GNU Affero General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package eu.dobos.jozef.gnret.gui.experiment.grid;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.TeXWordFinder;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpellCheckManager implements SpellCheckListener {
	protected static SpellChecker spellChecker = new SpellChecker();

	/*public static final String[] DICTIONARIES = { "altamer.0",
			"altamer.1", "altamer.2", "american.0", 
			"american.1", "american.2",
			"british.0", "british.1", "british.2", 
			"english.0", "english.1",
			"english.2", "english.3" 
		};*/
	
	public static final String[] DICTIONARIES = { "final.txt" };

	private boolean spellingErrorPresent = false;

	public SpellCheckManager() throws IOException {
		spellChecker.addSpellCheckListener(this);
		for (String dictionaryResource : DICTIONARIES) {
            SpellDictionaryHashMap dictionary = new SpellDictionaryHashMap(            		
                new InputStreamReader(
                    this.getClass().getClassLoader().getResourceAsStream(dictionaryResource)
                )
            );
            spellChecker.addDictionary(dictionary);
        }
	}

	public boolean isSpelledCorrectly(String word) {
		StringWordTokenizer texTok = new StringWordTokenizer(
				word.toLowerCase(), new TeXWordFinder());
		spellingErrorPresent = false;
		spellChecker.checkSpelling(texTok);
		return !spellingErrorPresent;
	}

	@Override
	public void spellingError(SpellCheckEvent event) {
		// event.ignoreWord(true); // adds word into ignored list
		spellingErrorPresent = true;
		//System.out.println("SpellCheckManager says invalid word is: "
			//	+ event.getInvalidWord());

	}

}
