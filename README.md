# Labor 03 IMSC - Jetpackkel az égbe és tovább ✨ 🚀 🌌

***Szerző: Püspök-Kiss Balázs***

## Bevezető

Az IMSC feladat célja, hogy bemutasson egy megoldást, ami a legmodernebb eszközöket alkalmazza a
fejlesztés során. A motiváció a laborok átdolgozása mögött az, hogy szerintem a jelenlegi laborok
többnyire elavult technológiákat használnak, amik egy kötelező rossz az ipar számára. A régi
technológiák támogatása szükséges, azonban mindig érdemes a jövőre figyelni, ott bővíteni a
jelenlegi tudását az embernek.

A dokumentáció segítségével egy olyan alkalmazást hozol létre, amit követendő példának ítélek meg.
A megoldás során szó fog esni ajánlott eszközökről, amik jelentősen megkönnyítették számomra a
fejlesztést, valamint nagyon hasznosnak ítélem meg őket és használatuk segíthet majd a házi feladat
megírásában is.

## Feladat előtti megjegyzés

Ez a feladat sok bónusz infót tartalmaz, aminek a használata a való életben nagyon kívánt, azonban
valószínűleg nem lesz számonkérve. A legjobb tudásom ellenére is azt kérem, hogy keressétek fel
gyakran a [Jetpack Compose] dokumentációját a hivatalos oldalán, sokkal több mindenről esik szó ott,
amire nekem nincs időm kitérni. StackOverflow is hasznos olykor, keresni a Google-ön egyet, ha
rögtön nem működik valami. Az előző labor (`PublicTransport`) IMSC feladatánál sok olyan hasznos
dolgot elmagyaráztam, amit ennél a labornál nem fogok kifejteni. Viszont ennek ismerete nem
feltétel a labor elvégzéséhez feltétlenül. Önálló feladat során inkább a hivatalos dokumentációkra
és Google-re lehet támaszkodni. Kreatív feladatokat próbáltam tervezni, mintsem nagyon érthetetlen
agyalósakat.

Az én második (e) laborom átírásakor észrevettem magamon azt, hogy elszalad velem a ló, minél jobb
megoldást szeretnék találni arra a problémára, amit a sima labor szerintem nem oldott meg jól.
Ahogy átgondolom a céljaimat e anyag írásával kapcsolatban, nem úgy kellene fogalmazzak, hogy
én átírom/átdolgozom a labort. Ezen feladatnak nemcsak az a célja, hogy bemutasson egy-két
feature-t, hanem egy jelentősebb célja az, hogy ezen megfelelő használatát mutassa be.

Az a kegyetlen igazság, hogy a laborok célja jelenleg **nem** az, hogy egy jó módon bemutassa azt,
hogy és mit kellene csinálni, hogy megoldjatok meg egy problémát, hanem az, hogy bemutasson egy-két
absztrakciót-elemet-megoldást olyan problémákra, amiket *production* környezetben soha nem szabadna
úgy megoldani, ahogy a laboron csinálják (elég szigorú vagyok magammal szemben, azt érdemes
hozzátenni). Mondok néhány példát, amit valószínűleg majd még néhány néhány labor után fogtok csak
megérteni:

- Soha nem használnék RecycleView.Adapter-t, forever ListAdapter ha listákat kell kezelni.
- Betiltanám a code-behind fajta programozást, valami architektúrát kellene tanítani, mint [MVVM],
  vagy [RainbowCake] (amit egyébként a magyar Braun Márton fejlesztett).
- Plain SQLite használatát is betiltanám, [Room]-mal kezdeném a perzisztens adattárolás tanítását.
- Kötelezővé tenném a [Bibliát][Kotlin Coding Conventions].
- Sokkal több (és jobb) 3rd party és Jetpack lib-et használnék.

A rant-et ezennel felfüggesztem.

## Setup ⚙

A kezdő projekt az `AndroidWallet.zip` fájlba van becsomagolva (ha be van valahova csomagolva).
Egy projekt setup-olása és felkonfigurálása fontos, de nem a világot szeretném megváltani, úgyhogy
annak az elmagyarázása majd egy másik napra marad. Mindenesetre megjegyzek néhány dolgot. *Át lehet
ugrani idő hiányában a Setup részt, de ez nem lesz hosszú. A setup kb. ugyanaz, mint a 2. labor
IMSC feladatánál, azonban van néhány különbség.

### [Proguard], R8 és [MaterialIcons]

A Module `build.gradle` fájlban feltűnik néhány különbség a default-hoz képest. `minifyEnabled`
lehetővé teszi a felesleges kód eltávolítását, a kód obfuszkációját, valamint optimalizálja azt.
`shrinkResources` eltávolítja a nem használt resource fájlokat, ezzel is helyet spórolva. A meglévő
fájlok minősége nem romlik! Ezeket a kulcsszavakat egyébként a [Proguard] és R8 biztosítja
számunkra. Ezek az eszközök optimalizálják a kódot és lekicsinyítik az alkalmazást igény szerint,
de sokkal többet tudnak, mint amiről itt szót ejtettem.

```groovy
android {
    //...
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true // removes unused resources from /res folder
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    //...
}
```

[MaterialIcons] egy elég nagy könyvtár, ami amellett, hogy nagyon hasznos, elég sok erőforrás van
benne kihasználatlanul. [Proguard] és R8 segít a fel nem használt részeket kihagyni az
alkalmazásból, ezáltal az gyorsabban települ és fut. Egyébként [MaterialIcons] extended verziója
a [Google Icons] teljes kínálatával fel van szerelve, így egy pici idő lehet, míg betölti a built-in
linter a dolgokat a suggestion-öknél, viszont minden elérhető programmatikusan, `ImageVector`-okban.

```groovy
dependencies {
    //...
    // Material design icons
    implementation "androidx.compose.material:material-icons-core:$compose_version"
    // Extended version contains all the Icons!
    implementation "androidx.compose.material:material-icons-extended:$compose_version"
    //...
}
```

## Lightspeed ☀ 💫 ✨ 🚀

<p align="center">
<img alt="MainActivityLayout" src="assets/MainActivityLayout.png" width="40%"/>
</p>

A labor jelentős része meg van írva, viszont van néhány rész, amit a képzelőerőtökre szerettem
volna hagyni. Kérlek olvassátok el és kísérletezzetek az adott részekkel, mert érdekesek lehetnek,
ha egy apró paramétert megváltoztattok és megfigyelitek a megváltozott viselkedést.

Lesz olyan feladat, ami kívánja azt, hogy rakjatok össze egy UI komponenst a már meglévő
composable-ökből! Ügyeltem arra, hogy ne legyen túlságosan nehéz, hogy a példákból lehessen
csipegetni, de ha elakadtok, vagy kísérletezni szeretnétek, akkor a [Jetpack Compose] dokumentációja
mindig kéznél van. Ha egy Compose-os alternatíváját szeretnétek megtudni egy XML és Fragment-es
elemhez, akkor [ez a honlap][Equivalent of X in Compose] kisegíthet titeket.

### Keyboard kezelése ⌨

<!---
TODO: Ki kellene cserélni ezt a képet!
--->
<p align="center">
<img alt="MainActivityLayout" src="assets/MainActivityLayout.png" width="40%"/>
</p>

`Snackbar` így jelenne meg ideális esetben.

Az alkalmazás feldob egy `SnackBar`-t, mikor hibás adatok kerülnek be a `TextField`-be. Ha éppen
meg van nyitva a szoftveres billentyűzet, akkor ez a billentyűzet alapesetben kitakarja a
`SnackBar`-t. ***Ez veszélyes!*** Szerencsére [Jetpack Compose]ban nagyon egyszerű a fix.
A `ui/theme/Theme.kt` file-ba, a *SideEffect* scope-jába be kell rakni az alábbi sort.

```kotlin
WindowCompat.setDecorFitsSystemWindows(window, false)
```

Azonban ez még nem oldja meg magába a problémát! Ahhoz, hogy a `SnackBar` jól reagálja le a keyboard
változását, meg kell mondani, melyik UI komponens-en legyen az az adott *`padding`*, ami
beleszámítja a billentyűzetet. Én 3 népszerű *`padding`*-et szoktam ilyenkor használni, amik
egyébként `Modifier`-ek:

```kotlin
Modifier
    .imePadding()
    .statusBarsPadding()
    .navigationBarsPadding()
```

Ezeket a paddingeket egyébként be is lehet illeszteni a `SnackbarHost` `Modifier` paraméteréhez.

### `TransactionCard` 💳

Hogy egy picit gyakoroljatok, üresen hagytam egy-két apró részt, pl. a `TransactionCard` composable
belsejét. Egyedileg testre tudjátok szabni, tudtok kísérletezni, hogy hogyan legyenek elrendezve
az `imageVector`-t és `color`-t felhasználó UI komponensek az `ElevatedCard`-on belül.
A fenti kép ad egy példát, hogy mit kellene alkotni, hogyan nézhet ki egy végleges layout.
Ezeknél a részeknél többnyire nincs rossz megoldás, csak legyenek megjelenítve az elvárt
információk, mint a `Transaction` neve és értéke.

### ViewModel lifecycle

[Jetpack Compose]: https://developer.android.com/jetpack/compose

[MVVM]: https://developer.android.com/topic/architecture

[RainbowCake]: https://rainbowcake.dev/

[Room]: https://developer.android.com/training/data-storage/room

[Kotlin Coding Conventions]: https://kotlinlang.org/docs/coding-conventions.html

[MaterialIcons]: https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary

[Proguard]: https://developer.android.com/studio/build/shrink-code

[Google Icons]: https://fonts.google.com/icons

[Equivalent of X in Compose]: https://www.jetpackcompose.app/What-is-the-equivalent-of-X-in-Jetpack-Compose