import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;

public class Result extends Composite {
	private Label lblResultauthor;
	private Label lblResultpublisher;
	private Label lblResultsubtitle;
	private Label lblResulttitle;
	private Text lblResultdescription;

	public Result(Composite parent, int style) {
		super(parent, style);
		
		Label lblTitle = new Label(this, SWT.NONE);
		lblTitle.setBounds(10, 204, 55, 15);
		lblTitle.setText("Title:");
		
		Label lblSubtitle = new Label(this, SWT.NONE);
		lblSubtitle.setText("Subtitle:");
		lblSubtitle.setBounds(10, 224, 55, 15);
		
		Label lblAuthor = new Label(this, SWT.NONE);
		lblAuthor.setText("Author(s):");
		lblAuthor.setBounds(10, 245, 55, 15);
		
		Label lblPublisher = new Label(this, SWT.NONE);
		lblPublisher.setText("Publisher:");
		lblPublisher.setBounds(10, 266, 55, 15);
		
		lblResultauthor = new Label(this, SWT.NONE);
		lblResultauthor.setText("No Result");
		lblResultauthor.setBounds(92, 245, 322, 15);
		
		lblResultpublisher = new Label(this, SWT.NONE);
		lblResultpublisher.setText("No Result");
		lblResultpublisher.setBounds(92, 266, 322, 15);
		
		lblResultsubtitle = new Label(this, SWT.NONE);
		lblResultsubtitle.setText("No Result");
		lblResultsubtitle.setBounds(92, 224, 322, 15);
		
		lblResulttitle = new Label(this, SWT.NONE);
		lblResulttitle.setText("No Result");
		lblResulttitle.setBounds(92, 204, 322, 15);
		
		lblResultdescription = new Text(this, SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		lblResultdescription.setText("No Result");
		lblResultdescription.setBounds(92, 287, 322, 123);
		
		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setText("Description:");
		lblDescription.setBounds(10, 287, 66, 15);
		
		Button btnBack = new Button(this, SWT.NONE);
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Search.getComposite().setVisible(true);
				Search.getResult().setVisible(false);
			}
		});
		btnBack.setBounds(179, 416, 75, 25);
		btnBack.setText("Back");
		
		try {
			searchBook();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void searchBook() throws MalformedURLException {
		URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:"+Search.getIsbn());
		try (InputStream is = url.openStream();  JsonReader rdr = Json.createReader(is)) {
			JsonObject obj = rdr.readObject();
			//if obj.containsKey("items") is false that means no result
			if(obj.containsKey("items")) {
				JsonArray results = obj.getJsonArray("items");
				for (JsonObject result : results.getValuesAs(JsonObject.class)) {
					//First, I check if "volumeInfo" object contains the key that I want to get. 
					//Show title.
					if(result.getJsonObject("volumeInfo").containsKey("title")) {
						lblResulttitle.setText(result.getJsonObject("volumeInfo").getString("title"));
					} else {
						lblResulttitle.setText("No title");
					}
					//Show subtitle.
					if(result.getJsonObject("volumeInfo").containsKey("subtitle")) {
						lblResultsubtitle.setText(result.getJsonObject("volumeInfo").getString("subtitle"));
					} else {
						lblResultsubtitle.setText("No subtitle");
					}
					//get authors Value from JsonArray using Iterator
					if(result.getJsonObject("volumeInfo").containsKey("authors")) {
						Iterator<JsonValue> auList = result.getJsonObject("volumeInfo").getJsonArray("authors").iterator();
						String authors = "";
						while (auList.hasNext()) {
							 JsonValue auJsVa = auList.next();
							 authors += auJsVa.toString()+((auList.hasNext())?", ":"");
						}
						lblResultauthor.setText(authors);
					} else {
						lblResultauthor.setText("No author");
					}
					//Show publisher
					if(result.getJsonObject("volumeInfo").containsKey("publisher")) {
						lblResultpublisher.setText(result.getJsonObject("volumeInfo").getString("publisher"));
					} else {
						lblResultpublisher.setText("No publisher");
					}
					//Show description
					if(result.getJsonObject("volumeInfo").containsKey("description")) {
						lblResultdescription.setText(result.getJsonObject("volumeInfo").getString("description"));
					} else {
						lblResultdescription.setText("No description");
					}
					//Insert picture in the composite
					Composite picComposite = new Composite(this, SWT.NONE);
					picComposite.setLayout(new GridLayout());
					String imageLocation = "", noImage = "https://upic.me/i/8k/noimage.jpg";
					if(result.getJsonObject("volumeInfo").containsKey("imageLinks")) {
						if(result.getJsonObject("volumeInfo").getJsonObject("imageLinks").containsKey("thumbnail")) {	
							imageLocation = result.getJsonObject("volumeInfo").getJsonObject("imageLinks").getString("thumbnail");
						}
						else imageLocation = noImage;
					}
					else imageLocation = noImage;
					URL urlImage = new URL(imageLocation);
					InputStream inImage = urlImage.openStream();
					Image image = new Image(Display.getCurrent(), inImage);
					picComposite.setBackgroundImage(image);
					picComposite.setBounds(21, 10, 130, 170);
				}//for
			}//if
		} catch (IOException e) {
			System.out.println("error!");
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
}
