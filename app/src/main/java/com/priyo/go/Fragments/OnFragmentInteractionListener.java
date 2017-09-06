package com.priyo.go.Fragments;

import android.os.Bundle;

/**
 * Created by sajid.shahriar on 4/11/17.
 */

public interface OnFragmentInteractionListener {

    void replaceWith(int changeToFragmentId);

    void replaceWith(int changeToFragmentId, Bundle bundle);

    void addToBackStack(int addFragmentId);

    void addToBackStack(int addFragmentId, Bundle bundle);

}
