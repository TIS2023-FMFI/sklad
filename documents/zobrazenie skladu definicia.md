Tento dokument obsahuje informácie potrebné pri vytváraní dokumentu pre pozície v sklade.
Dokument musí byť textový súbor (*.txt).

Štruktúra súboru musí byť presne dodržaná podľa definovaných pravidiel, inak načítanie nových pozícií z daného súboru nebude možné.



Každý riadok predstavuje jednu poličku v danom regály. Na začiatku každé riadka je písmeno, ktoré symbolizuje, 
či ide o vysoké alebo normálne pozície (v - vysoká, n - normálna).
Následne sa vypíšu jednotlivé názvy pozícií, ktoré sa v danej poličke nachádzajú.
Jednotilvé názvy sa oddeľujú pomlčkou '-'.
V prípade potreby, je možné poličku rozdeliť na viac riadkov, aby sa pre používateľa zlepšila čitateľosť súboru.

Vzorový príklad:

Majme textový dokument, ktorý bude vyzerať nasledovne:

n-A0011-A0013

n-A0032

n-A0022-A0023

v-A0041-A0043

V tomto prípade by sa vytvorilo 7 pozícií, z toho by 5 bolo normálnych a 2 by boli vysoké.
Ak by sme chceli rozdeliť napríklad riadok v-A0041-A0043 na viac záznamov, tak súbor by vyzeral nasledovne:

n-A0011-A0013

n-A0032

n-A0022-A0023

v-A0041

v-A0043


