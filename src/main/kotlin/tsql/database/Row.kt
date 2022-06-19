package tsql.database

data class Row(
    var startTime: Long = 0,
    var endTime: Long = Long.MAX_VALUE,
    var data: MutableList<Any> = mutableListOf()
) {


    fun deleteColumn(index: Int){
        if (index >= 0 && index< data.size){
             data.removeAt(index)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Row
        //
        // if (startTime != other.startTime) return false
        // if (endTime != other.endTime) return false
        var tdata = data.toMutableList()
        // tdata.removeLast()
        // tdata.removeLast()
        var otdata = other.data.toMutableList()
        // otdata.removeLast()
        // otdata.removeLast()
        if (tdata != otdata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = startTime.hashCode()
        result = 31 * result + endTime.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }
}
