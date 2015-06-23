package xmiLoadTest;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.XMI2UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

public class Main {

    public static void main(String[] args) {
        final String input = "model/Test.xmi";

        ResourceSet rs = new ResourceSetImpl();
        rs.setURIConverter(new CustomURIConverter());
        UMLResourcesUtil.init(rs);
        
        rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
                XMI2UMLResource.FILE_EXTENSION, XMI2UMLResource.Factory.INSTANCE);

        Resource resource = rs.getResource(createFileURI(input), true);
        System.out.println("Root objects count: " + resource.getContents().size());
        System.out.println("Root objects:");
        for (EObject obj : resource.getContents()) {
            System.out.println(obj);
        }
        Model uml = (Model)EcoreUtil.getObjectByType(
                resource.getContents(), UMLPackage.eINSTANCE.getModel());
        
        if (uml.getAppliedStereotypes().isEmpty()) {
            System.out.println("No stereotypes were found. Trying to apply a stereotype.");
            Stereotype stereotype = findStereotype(uml);
            if (stereotype != null) {
                uml.applyStereotype(stereotype);
            }
            else {
                System.out.println("Stereotype to apply not found!");
            }
        }
        else {
            for (Stereotype st : uml.getAppliedStereotypes()) {
                System.out.println("Applied stereotype: " + st);
            }
        }
    }
    
    private static Stereotype findStereotype(Model uml)
    {
        for (Profile profile : uml.getAllAppliedProfiles()) {
            if (profile.getName().equals("TestProfile")) {
                for (Stereotype st : profile.getOwnedStereotypes()) {
                    if (st.getName().equals("ModelStereotype")) {
                        return st;
                    }
                }
            }
        }
        return null;
    }

    private static URI createFileURI(String relativePath)
    {
        return URI.createFileURI(new File(relativePath).getAbsolutePath());
    }
}
