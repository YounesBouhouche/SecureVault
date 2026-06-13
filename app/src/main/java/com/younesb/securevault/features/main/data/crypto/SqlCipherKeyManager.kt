package com.younesb.securevault.features.main.data.crypto

import com.younesb.securevault.core.util.crypto.Crypto
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

class SqlCipherKeyManager(val crypto: Crypto) {
    fun getSupportFactory(): SupportOpenHelperFactory {
        return SupportOpenHelperFactory(crypto.getKey().encoded)
    }
}