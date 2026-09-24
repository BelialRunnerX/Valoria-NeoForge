## Contributing to the NeoForge 1.21.1 port

This repository is the NeoForge 1.21.1 port of Valoria and is maintained separately from the original project. Bug reports, fixes and translation updates for the port are handled **here**, through issues and pull requests in this repository — please do not send them to the original author.

## Always test your changes.
Do not submit something without at least running the game to see if it compiles.
If you are submitting a new block, make sure it has a translated name, and that it works correctly in-game. If you are changing existing block mechanics, test them out first.

### Translating tips
To begin you'll need to create a translation json  
Translation codes (can be found at your minecraft folder \assets\minecraft\lang) or [here](https://minecraft.wiki/w/Language)     
example file: en_us.json   

- regex that may help you find untranslated strings: `"\w+(\.\w+)*":\s*"[A-Za-z0-9 ,.!?'()\[\]%&*-]+",` (works perfect on cyrillic)
- Translation fixes for the port go in as a pull request (or an issue with the file attached) on this repository. New languages are best contributed to the original project as well, so both versions benefit.
- Translation parser:
  - New line - \n <p>
  - Hex Color - #96b5bf <p>
  - Italic - &i <p>
  - Bold - &b Bold <p>
  - Strikethrough - &s <p>
  - Underlined - &u <p>
  - Obfuscated - &k <p>
  - Reset of Formating - /& <p>


- Don't translate the mod name, it's not that necessarily.  
  Example for the correct one: "Valoria - Blocks" - "Valoria - Блоки"
- Translations aren't 100% accurate, so you can edit them to fix the mistakes
- After changing `en_us.json`, run `./gradlew updateResources` to propagate new keys to the other language files (the upstream repository did this automatically; this one does not).
