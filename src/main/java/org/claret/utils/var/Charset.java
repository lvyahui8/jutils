package org.claret.utils.var;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2016/11/23 11:24
 */
public enum Charset {
    GBK("gbk"),UTF8("utf-8"),UNKOWN("unkown");

    private String charsetName;

    Charset(String charsetName) {
        this.charsetName = charsetName;
    }

    @Override
    public String toString() {
        return this.charsetName;
    }
}
