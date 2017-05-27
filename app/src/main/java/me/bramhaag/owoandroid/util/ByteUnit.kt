package me.bramhaag.owoandroid.util

/*
 * Copyright 2011 Fabian Barney
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Fabian Barney
 * @author bramhaag - Converted to Kotlin and heavily modified everything to the point where it is almost not recognizable anymore
 */
enum class ByteUnit(var unit: String) {

    BYTE("B") {
        override fun toBytes(d: Double) = d
        override fun convert(d: Double, u: ByteUnit) = u.toBytes(d)
    },

    KB("kB") {
        override fun toBytes(d: Double) = safeMulti(d, C_KB)
        override fun convert(d: Double, u: ByteUnit) = u.toKB(d)
    },

    MB("MB") {
        override fun toBytes(d: Double) = safeMulti(d, C_MB)
        override fun convert(d: Double, u: ByteUnit) = u.toMB(d)
    },

    GB("GB") {
        override fun toBytes(d: Double) = safeMulti(d, C_GB)
        override fun convert(d: Double, u: ByteUnit) = u.toGB(d)
    };

    abstract fun toBytes(d: Double): Double
    abstract fun convert(d: Double, u: ByteUnit): Double

    fun toKB(d: Double) = toBytes(d) / C_KB
    fun toMB(d: Double) = toBytes(d) / C_MB
    fun toGB(d: Double) = toBytes(d) / C_GB

    companion object {
        val C_B: Double = 0.0
        val C_KB = Math.pow(10.0, 3.0)
        val C_MB = Math.pow(10.0, 6.0)
        val C_GB = Math.pow(10.0, 9.0)
    }

    fun safeMulti(d: Double, multi: Double): Double {
        val limit = Double.MAX_VALUE / multi

        if (d > limit) {
            return java.lang.Double.MAX_VALUE
        }
        if (d < -limit) {
            return java.lang.Double.MIN_VALUE
        }

        return d * multi
    }

}