package me.bramhaag.owoandroid.util

enum class ByteUnit(var unit: String) {

    BYTE("B") {
        override fun toBytes(d: Double) = d
        override fun convert(d: Double, u: ByteUnit) = u.toBytes(d)
    },

    KB("kB") {
        override fun toBytes(d: Double) = d * C_KB
        override fun convert(d: Double, u: ByteUnit) = u.toKB(d)
    },

    MB("MB") {
        override fun toBytes(d: Double) = d * C_MB
        override fun convert(d: Double, u: ByteUnit) = u.toMB(d)
    },

    GB("GB") {
        override fun toBytes(d: Double) = d * C_GB
        override fun convert(d: Double, u: ByteUnit) = u.toGB(d)
    };

    abstract fun toBytes(d: Double): Double
    abstract fun convert(d: Double, u: ByteUnit): Double

    fun toKB(d: Double) = toBytes(d) / C_KB
    fun toMB(d: Double) = toBytes(d) / C_MB
    fun toGB(d: Double) = toBytes(d) / C_GB

    companion object {
        val C_B = 0.0
        val C_KB = 10E3
        val C_MB = 10E6
        val C_GB = 10E9
    }
}