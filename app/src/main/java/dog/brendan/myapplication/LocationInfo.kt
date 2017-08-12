package dog.brendan.myapplication

data class State(val name: String, val shortname: String, val location: List<LocationInfo>)

data class LocationInfo(val name: String, val address: String,
                        val phone: String, val website: String, val email: String,
                        val camera: ArrayList<Camera>)

data class Camera(val name: String, val id: String)