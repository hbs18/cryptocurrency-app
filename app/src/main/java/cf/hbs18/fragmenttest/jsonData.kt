package cf.hbs18.fragmenttest

class jsonData : ArrayList<jsonDataItem>()

data class jsonDataItem(
    val `1d`: D,
    val circulating_supply: String,
    val currency: String,
    val first_candle: String,
    val first_order_book: String,
    val first_trade: String,
    val high: String,
    val high_timestamp: String,
    val id: String,
    val logo_url: String,
    val market_cap: String,
    val max_supply: String,
    val name: String,
    val num_exchanges: String,
    val num_pairs: String,
    val num_pairs_unmapped: String,
    val price: String,
    val price_date: String,
    val price_timestamp: String,
    val rank: String,
    val rank_delta: String,
    val status: String,
    val symbol: String
)

data class D(
    val market_cap_change: String,
    val market_cap_change_pct: String,
    val price_change: String,
    val price_change_pct: String,
    val volume: String,
    val volume_change: String,
    val volume_change_pct: String
)
