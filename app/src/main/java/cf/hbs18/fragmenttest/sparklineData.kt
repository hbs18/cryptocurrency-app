package cf.hbs18.fragmenttest

class sparklineData : ArrayList<sparklineDataItem>()

data class sparklineDataItem(
    val currency: String,
    val prices: List<String>,
    val timestamps: List<String>
)
