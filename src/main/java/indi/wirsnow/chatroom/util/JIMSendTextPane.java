package indi.wirsnow.chatroom.util;

import javax.swing.*;
import javax.swing.text.*;

/**
 * @author : wirsnow
 * @date : 2023/1/26 20:28
 * @description : 让JTextPane实现自动换行的子类, 原贴https://www.iteye.com/topic/1138181
 */
public class JIMSendTextPane extends JTextPane {

    /**
     * 重写JTextPane的构造方法
     */
    public JIMSendTextPane() {
        super();
        this.setEditorKit(new WarpEditorKit()); // 重写EditorKit
    }

    private static class WarpEditorKit extends StyledEditorKit {
        private final ViewFactory defaultFactory = new WarpColumnFactory();

        @Override
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }
    }

    private static class WarpColumnFactory implements ViewFactory {
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName -> {
                        return new WarpLabelView(elem);
                    }
                    case AbstractDocument.ParagraphElementName -> {
                        return new ParagraphView(elem);
                    }
                    case AbstractDocument.SectionElementName -> {
                        return new BoxView(elem, View.Y_AXIS);
                    }
                    case StyleConstants.ComponentElementName -> {
                        return new ComponentView(elem);
                    }
                    case StyleConstants.IconElementName -> {
                        return new IconView(elem);
                    }
                }
            }

            return new LabelView(elem);
        }
    }

    private static class WarpLabelView extends LabelView {
        public WarpLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            return switch (axis) {
                case View.X_AXIS -> 0;
                case View.Y_AXIS -> super.getMinimumSpan(axis);
                default -> throw new IllegalArgumentException("Invalid axis: " + axis);
            };
        }
    }
}