import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
		Display display = new Display();
		final Shell mainWindow = new Shell(display, SWT.MIN);
		
		final Text addressLine = new Text(mainWindow, SWT.SINGLE | SWT.CENTER);
		
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.wrap = true;
		layout.fill = true;
		layout.justify = false;
		mainWindow.setLayout(layout);
		
		Button startButton = new Button(mainWindow, SWT.PUSH);
		startButton.setText("START!");
		RowData data = new RowData();
		data.width = mainWindow.getClientArea().width;
		startButton.setLayoutData(data);
		
		final Browser browser = new Browser(mainWindow, SWT.WEBKIT);
		data = new RowData();
		data.width = 640;
		data.height = 480;
		browser.setLayoutData(data);
		
		
		
		
		addressLine.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Empty: never called.
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				loadAddress(addressLine, browser);
			}
		});
		
		
		System.out.println(display.getPrimaryMonitor().getClientArea().width);
		startButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadAddress(addressLine, browser);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//Empty: never called.
			}
		});
		
		mainWindow.pack();
		mainWindow.open();
		while (!mainWindow.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
