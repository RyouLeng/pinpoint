/*
 * Copyright 2015 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.web.calltree.span;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.navercorp.pinpoint.common.bo.SpanBo;
import com.navercorp.pinpoint.common.bo.SpanEventBo;

/**
 * 
 * @author jaehong.kim
 *
 */
public class CallTreeIteratorTest {
    private static final boolean SYNC = false;
    private static final boolean ASYNC = true;

    @Ignore
    @Test
    public void depth() {
        SpanAlign root = makeSpanAlign(1430983914531L, 240);
        CallTree callTree = new SpanCallTree(root);
        callTree.add(1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 0, 1, 1));
        callTree.add(2, makeSpanAlign(root.getSpanBo(), SYNC, (short) 1, 2, 1));
        callTree.add(3, makeSpanAlign(root.getSpanBo(), SYNC, (short) 2, 3, 1));
        callTree.add(4, makeSpanAlign(root.getSpanBo(), SYNC, (short) 3, 4, 1));
        callTree.add(-1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 4, 5, 1));
        callTree.add(2, makeSpanAlign(root.getSpanBo(), SYNC, (short) 5, 6, 1));
        callTree.add(3, makeSpanAlign(root.getSpanBo(), SYNC, (short) 6, 7, 1));
        callTree.add(-1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 7, 8, 1));
        callTree.add(1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 8, 9, 1));

        CallTreeIterator iterator = callTree.iterator();
        // assertEquals(5, iterator.size());

        while (iterator.hasNext()) {
            CallTreeNode node = iterator.next();
            for (int i = 0; i <= node.getDepth(); i++) {
                System.out.print("#");
            }
            System.out.println("");
        }
    }

    @Ignore
    @Test
    public void gap() {
        SpanAlign root = makeSpanAlign(1430983914531L, 240);
        CallTree callTree = new SpanCallTree(root);
        callTree.add(1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 0, 1, 1));
        callTree.add(2, makeSpanAlign(root.getSpanBo(), SYNC, (short) 1, 2, 1));
        callTree.add(3, makeSpanAlign(root.getSpanBo(), SYNC, (short) 2, 3, 1));
        callTree.add(4, makeSpanAlign(root.getSpanBo(), SYNC, (short) 3, 4, 1));
        callTree.add(-1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 4, 5, 1));
        callTree.add(2, makeSpanAlign(root.getSpanBo(), SYNC, (short) 5, 6, 1));
        callTree.add(3, makeSpanAlign(root.getSpanBo(), SYNC, (short) 6, 7, 1));
        callTree.add(-1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 7, 8, 1));
        callTree.add(1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 8, 9, 1));

        CallTreeIterator iterator = callTree.iterator();
        // assertEquals(5, iterator.size());

        while (iterator.hasNext()) {
            CallTreeNode node = iterator.next();
            for (int i = 0; i <= node.getDepth(); i++) {
                System.out.print("#");
            }
            System.out.println(" : gap=" + node.getGap());
        }
    }
    
    @Test
    public void gapAsync() {
        SpanAlign root = makeSpanAlign(1430983914531L, 240);
        CallTree callTree = new SpanCallTree(root);
        callTree.add(1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 0, 1, 1));
        callTree.add(2, makeSpanAlign(root.getSpanBo(), SYNC, (short) 1, 2, 1));
        callTree.add(3, makeSpanAlign(root.getSpanBo(), SYNC, (short) 2, 3, 1));
        callTree.add(4, makeSpanAlign(root.getSpanBo(), SYNC, (short) 3, 4, 1));
        
        CallTree subTree = new SpanAsyncCallTree(root);
        subTree.add(1, makeSpanAlign(root.getSpanBo(), ASYNC, (short) 0, 5, 1));
        subTree.add(2, makeSpanAlign(root.getSpanBo(), ASYNC, (short) 1, 6, 1));
        subTree.add(3, makeSpanAlign(root.getSpanBo(), ASYNC, (short) 2, 7, 1));
        subTree.add(4, makeSpanAlign(root.getSpanBo(), ASYNC, (short) 3, 8, 1));
        callTree.add(subTree);
        
        callTree.add(-1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 4, 5, 1));
        callTree.add(2, makeSpanAlign(root.getSpanBo(), SYNC, (short) 5, 6, 1));
        callTree.add(3, makeSpanAlign(root.getSpanBo(), SYNC, (short) 6, 7, 1));
        callTree.add(-1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 7, 8, 1));
        callTree.add(1, makeSpanAlign(root.getSpanBo(), SYNC, (short) 8, 9, 1));

        CallTreeIterator iterator = callTree.iterator();
        // assertEquals(5, iterator.size());

        while (iterator.hasNext()) {
            CallTreeNode node = iterator.next();
            for (int i = 0; i <= node.getDepth(); i++) {
                System.out.print("#");
            }
            System.out.println(" : gap=" + node.getGap());
        }
    }


    private SpanAlign makeSpanAlign(long startTime, int elapsed) {
        SpanBo span = new SpanBo();
        span.setStartTime(startTime);
        span.setElapsed(elapsed);

        return new SpanAlign(new SpanBo());
    }

    private SpanAlign makeSpanAlign(SpanBo span, final boolean async, final short sequence, int startElapsed, int endElapsed) {
        return makeSpanAlign(span, async, sequence, startElapsed, endElapsed, -1, -1);
    }

    private SpanAlign makeSpanAlign(SpanBo span, final boolean async, final short sequence, int startElapsed, int endElapsed, final int asyncId, int nextAsyncId) {
        SpanEventBo event = new SpanEventBo();
        event.setSequence(sequence);
        event.setStartElapsed(startElapsed);
        event.setEndElapsed(endElapsed);
        event.setAsyncId(asyncId);
        event.setNextAsyncId(nextAsyncId);

        return new SpanAlign(span, event);
    }
}