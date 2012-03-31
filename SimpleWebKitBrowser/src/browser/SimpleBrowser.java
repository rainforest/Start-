package browser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class SimpleBrowser {

	/**
	 * @param args
	 */
	static void loadAddress(Text addressLine, Browser browser) {
		String content = addressLine.getText();
		if (content.equals("")) {
			addressLine.setText("http://www.google.com");
		} else if (!content.contains("http://")) {
			addressLine.setText("http://" + content);
		}
		browser.setUrl(addressLine.getText());
	}
	
	public static void main(String[] args) {
		//Create display and main shell.
		Display display = new Display();
		final Shell mainWindow = new Shell(display);
		mainWindow.setMinimumSize(SimpleBrowserConstants.MIN_WINDOW_WIDTH, SimpleBrowserConstants.MIN_WINDOW_HEIGHT);
		mainWindow.setLocation(display.getClientArea().x +
				(display.getClientArea().width - mainWindow.getSize().x) / 2, 
				display.getClientArea().y + 
				(display.getClientArea().height - mainWindow.getSize().y) / 2);
		
		//Prepare main layout.
		final RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.wrap = false;
		layout.fill = true;
		layout.justify = false;
		mainWindow.setLayout(layout);
		
		//Create address line and START! button.
		final Text addressLine = new Text(mainWindow, SWT.SINGLE | SWT.CENTER);
		final Button startButton = new Button(mainWindow, SWT.PUSH);
		startButton.setText("START!");
		RowData data = new RowData();
		startButton.setLayoutData(data);
		
		//Create browser.
		final Browser browser = new Browser(mainWindow, SWT.WEBKIT);
		data = new RowData();
		data.width = SimpleBrowserConstants.DEFAULT_BROWSER_SIZE_X;
		data.height = SimpleBrowserConstants.DEFAULT_BROWSER_SIZE_Y;
		browser.setLayoutData(data);
		
		//Add SelectionListener for the START! button and address line.
		addressLine.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				loadAddress(addressLine, browser);
			}
		});
		
		startButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				loadAddress(addressLine, browser);
			}
			
		});

		//Add main window's listener to control resizing of components.
		mainWindow.addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent e) {
				System.out.println("Resized!");
				System.out.println(mainWindow.getClientArea());
				int widgetsWidth = mainWindow.getClientArea().width - 
						(layout.marginLeft + layout.marginRight + layout.spacing * 2);
				RowData rowData = new RowData(widgetsWidth, SWT.DEFAULT);
				addressLine.setLayoutData(rowData);
				startButton.setLayoutData(rowData);
				System.out.println(addressLine.getSize().y + startButton.getSize().y);
				browser.setLayoutData(new RowData(widgetsWidth, 
						mainWindow.getClientArea().height -
						(addressLine.computeSize(widgetsWidth, SWT.DEFAULT).y +
								startButton.computeSize(widgetsWidth, SWT.DEFAULT).y + layout.marginTop * 3)));
				browser.redraw();
				System.out.println(browser.getLayoutData());
				System.out.println(layout.spacing);
			}
			
			@Override
			public void controlMoved(ControlEvent e) {
				// Empty.
			}
		});
		
		//Run.
		mainWindow.pack();
		mainWindow.open();
		while (!mainWindow.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
