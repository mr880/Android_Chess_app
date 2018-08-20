package com.example.mkasa.chessApp67;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class ObjectStream {

    private ObjectStream(){}

    public static Object copyAll(Object oldObj) throws Exception
    {

        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            // serialize and pass the object
            oos.writeObject(oldObj);
            oos.flush();

            ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bin);

            Object result = ois.readObject();

            oos.close();
            ois.close();

            // return the new object
            return result;
        }
        catch (Exception e)
        {
            throw new Exception(e);
        }
    }
}

