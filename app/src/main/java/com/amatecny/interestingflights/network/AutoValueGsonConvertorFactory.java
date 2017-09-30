package com.amatecny.interestingflights.network;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * See <a href="https://github.com/rharter/auto-value-gson#factory">Autovalue Gson convertor factory</a>
 * <p>
 * Created by amatecny on 28/09/2017
 */
@GsonTypeAdapterFactory
public abstract class AutoValueGsonConvertorFactory implements TypeAdapterFactory {

    public static AutoValueGsonConvertorFactory create() {
        return new AutoValueGson_AutoValueGsonConvertorFactory();
    }
}
