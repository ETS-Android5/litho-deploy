/**
 * Copyright (c) 2014-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.facebook.litho.SizeSpec;
import com.facebook.litho.utils.IncrementalMountUtils;

import static com.facebook.litho.SizeSpec.UNSPECIFIED;

public abstract class LinearComponentBinder extends
    RecyclerComponentBinder<
        LinearLayoutManager,
        RecyclerComponentBinder.RecyclerComponentWorkingRangeController>
    implements HasStickyHeader {

  private int mInitFillMainAxis;

  public LinearComponentBinder(Context context, LinearLayoutManager layoutManager) {
    this(context, layoutManager, new RecyclerComponentWorkingRangeController());
  }

  public LinearComponentBinder(Context context, LinearLayoutManager layoutManager,
      RecyclerComponentWorkingRangeController rangeController) {
    super(context, layoutManager, rangeController);
  }

  @Override
  protected void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    final LinearLayoutManager layoutManager = getLayoutManager();
    final int firstViewPosition = layoutManager.findFirstVisibleItemPosition();
    final int lastViewPosition = layoutManager.findLastVisibleItemPosition();

    if (firstViewPosition == RecyclerView.NO_POSITION ||
        lastViewPosition == RecyclerView.NO_POSITION) {
      return;
    }

    if (isIncrementalMountEnabled()) {
      IncrementalMountUtils.performIncrementalMount(recyclerView);
    }

    getRangeController().notifyOnScroll(
        firstViewPosition,
        lastViewPosition - firstViewPosition + 1);
  }

  @Override
  protected int getWidthSpec(int position) {
    if (getLayoutManager().getOrientation() == LinearLayoutManager.VERTICAL) {
      return super.getWidthSpec(position);
    } else {
      return SizeSpec.makeSizeSpec(0, UNSPECIFIED);
    }
  }

  @Override
  protected int getHeightSpec(int position) {
    if (getLayoutManager().getOrientation() == LinearLayoutManager.VERTICAL) {
      return SizeSpec.makeSizeSpec(0, UNSPECIFIED);
