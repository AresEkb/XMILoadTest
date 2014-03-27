package xmiLoadTest;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resource.XMI2UMLResource;

public class Main {

    public static void main(String[] args) {
        final String input = "model/Test.xmi";

        ResourceSet rs = new ResourceSetImpl();
        rs.setURIConverter(new CustomURIConverter());
        rs.getPackageRegistry().put(
                "http://www.eclipse.org/uml2/2.1.0/UML", UMLPackage.eINSTANCE);
        rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
                UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
        rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
                XMI2UMLResource.FILE_EXTENSION, XMI2UMLResource.Factory.INSTANCE);

        Resource resource = rs.getResource(createFileURI(input), true);
        System.out.println("Root: " + resource.getContents().get(0).getClass());
        System.out.println("Count: " + resource.getContents().size());
        Model uml = (Model)EcoreUtil.getObjectByType(
                resource.getContents(), UMLPackage.eINSTANCE.getModel());
        for (Profile profile : uml.getAllAppliedProfiles()) {
            System.out.println(profile);
//            for (Element el : profile.allOwnedElements()) {
//                System.out.println(el);
//            }
        }
        for (Stereotype stereotype : uml.getAppliedStereotypes()) {
            System.out.println(stereotype);
        }
    }

    private static URI createFileURI(String relativePath)
    {
        return URI.createFileURI(new File(relativePath).getAbsolutePath());
    }
}
