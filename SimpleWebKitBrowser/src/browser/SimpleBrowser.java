package browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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
			addressLine.setText(SimpleBrowserConstants.DEFAULT_URL);
		}
		browser.setUrl(addressLine.getText());
	}
	
	public static void main(String[] args) {
		//Create display and main shell.
		Display display = new Display();
		final Shell mainWindow = new Shell(display);
		mainWindow.setMinimumSize(SimpleBrowserConstants.MIN_WINDOW_WIDTH,
				SimpleBrowserConstants.MIN_WINDOW_HEIGHT);
		mainWindow.setLocation(display.getClientArea().x +
				(display.getClientArea().width - mainWindow.getSize().x) / 2, 
				display.getClientArea().y + 
				(display.getClientArea().height - mainWindow.getSize().y) / 2);
		mainWindow.setImage(new Image(display, "AppIcon.png"));
		mainWindow.setBackgroundMode(SWT.INHERIT_FORCE);
		
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
		mainWindow.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				int widgetsWidth = mainWindow.getClientArea().width - 
						(layout.marginLeft + layout.marginRight + layout.spacing * 2);
				RowData rowData = new RowData(widgetsWidth, SWT.DEFAULT);
				addressLine.setLayoutData(rowData);
				startButton.setLayoutData(rowData);
				browser.setLayoutData(new RowData(widgetsWidth, 
						mainWindow.getClientArea().height -
						(addressLine.computeSize(widgetsWidth, SWT.DEFAULT).y +
								startButton.computeSize(widgetsWidth, SWT.DEFAULT).y + layout.marginTop * 3)));
				browser.redraw();
			}
		});
		
		//Add browser's listener to show a current address in the address line.
		browser.addLocationListener(new LocationAdapter() {

			@Override
			public void changed(LocationEvent event) {
				addressLine.setText(browser.getUrl());
			}
		});
		
		//Browser's listener to show a links under the mouse pointer.
		browser.addStatusTextListener(new StatusTextListener() {
			
			@Override
			public void changed(StatusTextEvent event) {
				if (event.text != "")
					addressLine.setText(event.text);
				else
					addressLine.setText(browser.getUrl());
			}
		});
		
		//Shortcut listener.
		KeyListener addressSelectListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.F6) {
					addressLine.forceFocus();
					addressLine.selectAll();
				}
			}
		};
		mainWindow.addKeyListener(addressSelectListener);
		browser.addKeyListener(addressSelectListener);
		addressLine.addKeyListener(addressSelectListener);
		startButton.addKeyListener(addressSelectListener);
		
		//Add listener for a webpage title.
		browser.addTitleListener(new TitleListener() {
			
			@Override
			public void changed(TitleEvent event) {
				mainWindow.setText(event.title);
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
		display.dispose();
	}
}
