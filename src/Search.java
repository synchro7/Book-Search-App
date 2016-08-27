import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;

public class Search {

	protected Shell shlIsbnBookSearch;
	private static Composite composite;
	private static Result result;
	private static String isbn;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Search window = new Search();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlIsbnBookSearch.open();
		shlIsbnBookSearch.layout();
		while (!shlIsbnBookSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlIsbnBookSearch = new Shell();
		shlIsbnBookSearch.setSize(450, 505);
		shlIsbnBookSearch.setText("ISBN Book Search App");
		
		composite = new Composite(shlIsbnBookSearch, SWT.NONE);
		composite.setBounds(0, 0, 434, 466);
		
		Text textSearch = new Text(composite, SWT.BORDER);
		textSearch.setLocation(127, 188);
		textSearch.setSize(167, 21);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLocation(75, 191);
		lblNewLabel.setSize(41, 15);
		lblNewLabel.setText("ISBN #");
		
		Button btnSearch = new Button(composite, SWT.NONE);
		btnSearch.setLocation(303, 186);
		btnSearch.setSize(75, 25);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				isbn = textSearch.getText();
				if(isbn.equals("")) {
					MessageBox messageBox = new MessageBox(shlIsbnBookSearch, SWT.OK);
					messageBox.setText("Warning!!!");
					messageBox.setMessage("ISBN# cannot be blank!!!");
					messageBox.open();
				}
				else {
					composite.setVisible(false);
					result = new Result(shlIsbnBookSearch, SWT.NONE);
					result.setVisible(true);
					result.setBounds(composite.getBounds());
				}
			}
		});
		btnSearch.setText("Search");

	}

	public static Composite getComposite() {
		return composite;
	}

	public static void setComposite(Composite composite) {
		Search.composite = composite;
	}

	public static Result getResult() {
		return result;
	}

	public static void setResult(Result result) {
		Search.result = result;
	}

	public static String getIsbn() {
		return isbn;
	}

	public static void setIsbn(String isbn) {
		Search.isbn = isbn;
	}
	
}
