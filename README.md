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
amire nekem nincs időm kitérni. Stackoverflow is hasznos olykor, keresni a Google-ön egyet, ha
rögtön nem működik valami.

A második (e) labor átírásakor észrevettem magamon azt, hogy elszalad velem a ló, minél jobb
megoldást szeretnék találni arra a problémára, amit a sima labor szerintem nem oldott meg jól.
Ahogy átgondolom a céljaimat e anyag írásával kapcsolatban, nem úgy kellene fogalmazzak, hogy
én átírom/átdolgozom a labort. Ezen feladatnak nemcsak az a célja, hogy bemutasson egy-két
feature-t, hanem egy jelentősebb célja az, hogy ezen megfelelő használatát mutassa be.

Az a kegyetlen igazság, hogy a laborok célja jelenleg **nem** az, hogy egy jó módon bemutassa azt,
hogy és mit kellene csinálni, hogy megoldjatok egy problémát, hanem az, hogy bemutasson egy-két
absztrakciót-elemet-megoldást olyan problémákra, amiket *production* környezetben soha nem szabadna
úgy megoldani, ahogy a laboron csinálják (elég szigorú vagyok magammal szemben, azt érdemes
hozzátenni). Mondok néhány példát, amit valószínűleg majd néhány még néhány labor után fogtok csak
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

### Proguard és R8 és MaterialIcons

```groovy
buildTypes {
    debug {
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
    release {
        minifyEnabled true
        shrinkResources true // removes unused resources from /res folder
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### ViewModel lifecycle

[Jetpack Compose]: https://developer.android.com/jetpack/compose

[MVVM]: https://developer.android.com/topic/architecture

[RainbowCake]: https://rainbowcake.dev/

[Room]: https://developer.android.com/training/data-storage/room

[Kotlin Coding Conventions]: https://kotlinlang.org/docs/coding-conventions.html