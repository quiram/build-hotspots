package com.github.quiram.buildhotspots.visualisation;

import com.github.quiram.buildhotspots.drawingdata.Root;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.InputStream;

import static java.lang.ClassLoader.getSystemClassLoader;

public class FileBasedBuildHotspotsApplication extends BuildHotspotsApplicationBase {
    public static void main(String[] args) {
        System.out.println("Start BuildHotspotsApplication");
        launch(args);
        System.out.println("End BuildHotspotsApplication");
    }

    @Override
    protected Root getRootDocument(String source) {
        InputStream input = new BufferedInputStream(getSystemClassLoader().getResourceAsStream(source));

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            return (Root) jaxbUnmarshaller.unmarshal(input);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
