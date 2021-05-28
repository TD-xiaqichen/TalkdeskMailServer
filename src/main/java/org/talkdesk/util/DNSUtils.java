package org.talkdesk.util;

import org.xbill.DNS.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class DNSUtils {

    public static String getSMTPAddressByRecipient(String recipient) throws TextParseException, UnknownHostException {
        String domain = recipient.substring(recipient.indexOf("@")+1);
        Lookup lookup =new Lookup(domain, Type.MX);
        Record[] run = lookup.run();
        List<String> mxRecordsRaw = findMXRecordsRaw(run);
        InetAddress inetAddress = InetAddress.getByName(mxRecordsRaw.get(0));
        String hostAddress = inetAddress.getHostAddress();
        return hostAddress;
    }

    private static List<String> findMXRecordsRaw(Record[] answers ){
        List<String> servers = new ArrayList<>();
        if (answers == null) {
            return servers;
        }

        MXRecord[] mxAnswers = new MXRecord[answers.length];

        for (int i = 0; i < answers.length; i++) {
            mxAnswers[i] = (MXRecord) answers[i];
        }
        Comparator<MXRecord> mxComparator = Comparator.comparing(MXRecord::getPriority);
        Arrays.sort(mxAnswers, mxComparator);
        int currentPrio = -1;
        List<String> samePrio = new ArrayList<>();
        for (int i = 0; i < mxAnswers.length; i++) {
            boolean same = false;
            boolean lastItem = i + 1 == mxAnswers.length;
            MXRecord mx = mxAnswers[i];
            if (i == 0) {
                currentPrio = mx.getPriority();
            } else {
                same = currentPrio == mx.getPriority();
            }

            String mxRecord = mx.getTarget().toString();
            if (same) {
                samePrio.add(mxRecord);
            } else {
                Collections.shuffle(samePrio);
                servers.addAll(samePrio);

                samePrio.clear();
                samePrio.add(mxRecord);

            }

            if (lastItem) {
                Collections.shuffle(samePrio);
                servers.addAll(samePrio);
            }
        }
        return servers;
    }

}
