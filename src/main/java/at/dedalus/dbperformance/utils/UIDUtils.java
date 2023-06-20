/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), hosted at https://github.com/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * Agfa Healthcare.
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package at.dedalus.dbperformance.utils;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 */
public class UIDUtils {

    /**
     * UID root for UUIDs (Universally Unique Identifiers) generated in
     * accordance with Rec. ITU-T X.667 | ISO/IEC 9834-8.
     * @see &lt;a href="http://www.oid-info.com/get/2.25">OID repository {joint-iso-itu-t(2) uuid(25)}$lt;/a>
     */
    private static final String UUID_ROOT = "2.25";

    private static final Pattern PATTERN =
            Pattern.compile("[012]((\\.0)|(\\.[1-9]\\d*))+");

    private static final Charset ASCII = Charset.forName("US-ASCII");

    private static String root = UUID_ROOT;

    public static final String getRoot() {
        return root;
    }

    public static final void setRoot(String root) {
        checkRoot(root);
        UIDUtils.root = root;
    }

    private static void checkRoot(String root) {
        if (root.length() > 24)
            throw new IllegalArgumentException("root length > 24");
        if (!isValid(root))
            throw new IllegalArgumentException(root);
    }

    public static boolean isValid(String uid) {
        return uid.length() <= 64 && PATTERN.matcher(uid).matches();
    }

    public static String createUID() {
        return randomUID(root);
    }

    public static String createNameBasedUID(byte[] name) {
        return nameBasedUID(name, root);
    }

    public static String createNameBasedUID(byte[] name, String root) {
        checkRoot(root);
        return nameBasedUID(name, root);
    }

    public static String createUID(String root) {
        checkRoot(root);
        return randomUID(root);
    }

    public static String createUIDIfNull(String uid) {
        return uid == null ? randomUID(root) : uid;
    }

    public static String createUIDIfNull(String uid, String root) {
        checkRoot(root);
        return uid == null ? randomUID(root) : uid;
    }

    public static String remapUID(String uid) {
        return nameBasedUID(uid.getBytes(ASCII), root);
    }

    public static String remapUID(String uid, String root) {
        checkRoot(root);
        return nameBasedUID(uid.getBytes(ASCII), root);
    }

    private static String randomUID(String root) {
        return toUID(root, UUID.randomUUID());
    }

    private static String nameBasedUID(byte[] name, String root) {
        return toUID(root, UUID.nameUUIDFromBytes(name));
    }

    private static String toUID(String root, UUID uuid) {
        byte[] b17 = new byte[17];
        ByteUtils.longToBytesBE(uuid.getMostSignificantBits(), b17, 1);
        ByteUtils.longToBytesBE(uuid.getLeastSignificantBits(), b17, 9);
        String uuidStr = new BigInteger(b17).toString();
        int rootlen = root.length();
        int uuidlen = uuidStr.length();
        char[] cs = new char[rootlen + uuidlen + 1];
        root.getChars(0, rootlen, cs, 0);
        cs[rootlen] = '.';
        uuidStr.getChars(0, uuidlen, cs, rootlen + 1);
        return new String(cs);
    }

}
