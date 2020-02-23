package com.sq.transportmanage.gateway.service.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyValuePair<K,V>{
    K key;
    V value;

    boolean equals(KeyValuePair<K,V> pair) {
        return pair.key == this.key;
    }

    @Override
    public String toString() {
        return key.toString() + "=" + value.toString();
    }
}