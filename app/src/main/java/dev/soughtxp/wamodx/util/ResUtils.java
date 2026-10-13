package dev.soughtxp.wamodx.util;

import android.content.Context;

import dev.soughtxp.wamodx.App;

@SuppressWarnings("deprecation")
public enum ResUtils {

    DRAWABLE("drawable") {
        @Override
        Object resolve(Context ctx, int id) {
            return ctx.getResources().getDrawable(id);
        }
    },
    STRING("string") {
        @Override
        Object resolve(Context ctx, int id) {
            return ctx.getResources().getString(id);
        }
    },
    COLOR("color") {
        @Override
        Object resolve(Context ctx, int id) {
            return ctx.getResources().getColor(id);
        }
    },
    LAYOUT("layout") {
        @Override
        Object resolve(Context ctx, int id) {
            return id;
        }
    },
    ID("id") {
        @Override
        Object resolve(Context ctx, int id) {
            return id;
        }
    };

    private final String type;

    ResUtils(String type) {
        this.type = type;
    }

    abstract Object resolve(Context ctx, int id);

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        Context context = App.getContext();
        if (context == null) {
            throw new IllegalStateException("Context not available");
        }

        int id = context.getResources().getIdentifier(name, type, context.getPackageName());
        if (id == 0) {
            throw new IllegalArgumentException("Resource not found: " + type + "/" + name);
        }

        return (T) resolve(context, id);
    }
}
