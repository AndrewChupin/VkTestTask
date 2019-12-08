package com.vk.task.presentation.screens.result

import com.vk.core.utils.diff.SingleDiffUtil
import com.vk.task.data.game.GameResult


class ResultDiffUtils: SingleDiffUtil<GameResult>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val safeNew = itemNew
        val safeOld = itemOld

        if (safeNew == null || safeOld == null) {
            return false
        }

        if (oldItemPosition == 0 && newItemPosition == 0) {
            return safeNew.title == safeOld.title
                    && safeNew.earnedPoints == safeOld.earnedPoints
                    && safeNew.totalPoints == safeNew.totalPoints
        }

        // fast corporation, but has pliability of collision
        if (safeNew.answers[newItemPosition - 1].hashCode()
            == safeNew.answers[oldItemPosition - 1].hashCode()) {
            return true
        }

        return false
    }

    override fun getOldListSize(): Int {
        val safeOld = itemOld ?: return 0
        return safeOld.answers.size + 1
    }

    override fun getNewListSize(): Int {
        val safeNew = itemNew ?: return 0
        return safeNew.answers.size + 1
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val safeNew = itemNew
        val safeOld = itemOld

        if (safeNew == null || safeOld == null) {
            return false
        }

        if (oldItemPosition == 0 && newItemPosition == 0) {
            return safeNew.title == safeOld.title
                    && safeNew.earnedPoints == safeOld.earnedPoints
                    && safeNew.totalPoints == safeNew.totalPoints
        }

        if (safeNew.answers[newItemPosition - 1] == safeNew.answers[oldItemPosition - 1]) {
            return true
        }

        return false
    }
}
