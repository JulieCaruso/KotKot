package com.application.kotkot.utils.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.application.kotkot.R

import kotlinx.android.synthetic.main.item_loader.*

class LoadingDelegateAdapter: ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = LoaderViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {

    }

    class LoaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false) )
}