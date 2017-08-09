package dog.brendan.myapplication

class LocationManager {

    val commerce = Location("Commerce",
            "02daff262ecc43e69d182aa53fba88c3", "7efbc97cd62d44f695b6de175a0de1d0",
            "1f3860fa5d3a47eba1c218bfee3be323", "58aa97e71dd74e6cb2e01a16ea0b29f7")
    val bloomfield = Location("Bloomfield",
            "f7b0f8caea3141bd97e7027513763630", "56ccfcbf166b4461bce1c0f67715af58",
            "44c79ecd8f724223822346c6ea65cffe", "14153e6bd6b040b0aa6797e0fe0c4bed")
    val utica = Location("Utica",
            "7a081a594eb84b2f966d58fecfc22acd", "20f83ccc9bf14e49b4a92f822b8e4846",
            "517fa913f9ab4dfa8733d6912c32bf60", "d68098994d134e12a77ce3ed17676d69")
    val scottsdale = Location("Scottsdale",
            "ff5aaa225f4e48c4ac211256cb07c1ad", "e5b12c95e31241b481779a31c954a924",
            "05a6a341aba84ae8abfba1cd7c5cb036", "NONE")

    val coquitlam = Location("Coquitlam",
            "b99e33e40ced4b8999752effd8267175", "fa7d9a34fed2421592467412213f6703",
            "NONE", "6f6ef1a30847404ca3f9eb0568dc1d63")

    data class Location(val name: String, val toybox: String, val gym: String, val romper: String, val outside: String)

    val locations = arrayListOf<Location>(commerce, bloomfield, utica, scottsdale, coquitlam)
    val locationNames = arrayListOf<String>()

    init {
        for ((name) in locations) {
            locationNames.add(name)
        }
    }
}