//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.clickgui.skeet;

public enum Direction {
    FORWARDS,
    BACKWARDS;

    private Direction() {
    }

    public Direction opposite() {
        return this == FORWARDS ? BACKWARDS : FORWARDS;
    }
}
