package com.deange.quicklink.task;

import android.os.AsyncTask;

import com.deange.quicklink.model.BaseModel;

public abstract class BaseTask<Params, Progress, Model extends BaseModel> extends AsyncTask<Params, Progress, Model> {
    // Wrapper for possible future additions
}
