// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.*;

public class FlexibleSearchBetweenPredicateImpl extends ASTWrapperPsiElement implements FlexibleSearchBetweenPredicate {

  public FlexibleSearchBetweenPredicateImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull FlexibleSearchVisitor visitor) {
    visitor.visitBetweenPredicate(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FlexibleSearchVisitor) accept((FlexibleSearchVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<FlexibleSearchRowValuePredicand> getRowValuePredicandList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchRowValuePredicand.class);
  }

}