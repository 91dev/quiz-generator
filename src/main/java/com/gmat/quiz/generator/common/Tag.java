package com.gmat.quiz.generator.common;

/**
 * @author kapil.dev
 */
public enum Tag {
    Tag1("Tag1"),Tag2("Tag2"),Tag3("Tag3"),Tag4("Tag4"),Tag5("Tag5"),Tag6("Tag6");

    private String tag;

    Tag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public static Tag getEnumTagByStringTag(String tag){
        for(Tag t : Tag.values()){
            if(tag.equalsIgnoreCase(t.getTag())) return t;
        }
        return Tag.Tag1;
    }

    public Tag next() {
        switch (this) {
            case Tag1:
                return Tag2;
            case Tag2:
                return Tag3;
            case Tag3:
                return Tag4;
            case Tag4:
                return Tag5;
            case Tag5:
                return Tag6;
            case Tag6:
                return Tag1;
            default:
                return Tag1;
        }
    }
}
