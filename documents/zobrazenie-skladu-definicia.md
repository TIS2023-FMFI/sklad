Tento dokument obsahuje informácie potrebné pri vytváraní dokumentu pre pozície v sklade.
Dokument musí byť textový súbor (*.txt).

Štruktúra súboru musí byť presne dodržaná podľa definovaných pravidiel, inak načítanie nových pozícií z daného súboru nebude možné.


Každý riadok predstavuje jednu poličku v danom regáli. Na začiatku každé riadka je písmeno, ktoré symbolizuje, 
či ide o vysoké alebo normálne pozície (v - vysoká, n - normálna).
Následne sa vypíšu jednotlivé názvy pozícií, ktoré sa v danej poličke nachádzajú.
Jednotilvé názvy sa oddeľujú pomlčkou '-'.

Vzorový príklad:
Majme textový dokument, ktorý bude vyzerať nasledovne:

n-E0475-E0455-E0435
n-E0474-E0454-E0434
n-E0473-E0453-E0433
v-E0471-E0451-E0431
v-E0470-E0450-E0430

Reprezentuje rad E, ktorý sa skladá z 2 vysokých regálov a 3 normálnych. Ak by bolo potrebné tento regál prerobiť spôsobom, že by pozostával iba z normálnych regálov (2 vysoké regále by boli prerobené na 3 normálne). Textový dokument by sa mal zmeniť nasledovne:

n-E0475-E0455-E0435
n-E0474-E0454-E0434
n-E0473-E0453-E0433
n-E0472-E0452-E0432
n-E0471-E0451-E0431
n-E0470-E0450-E0430
