/*
 * File: app/view/MyViewport.js
 *
 * This file was generated by Sencha Architect version 2.1.0.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.1.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.1.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('MyApp.view.MyViewport', {
    extend: 'Ext.container.Viewport',

    layout: {
        type: 'border'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    region: 'west',
                    id: 'MenuPanelID',
                    width: 255,
                    layout: {
                        type: 'accordion'
                    },
                    collapsed: true,
                    collapsible: true,
                    title: 'תפריט',
                    items: [
                        {
                            xtype: 'form',
                            width: 148,
                            layout: {
                                align: 'stretch',
                                type: 'vbox'
                            },
                            collapsed: false,
                            title: 'מכרונים',
                            dockedItems: [
                                {
                                    xtype: 'textfield',
                                    flex: 1,
                                    dock: 'top',
                                    id: 'txtPrefixID',
                                    margin: 10,
                                    fieldLabel: 'Prefix',
                                    labelWidth: 50
                                },
                                {
                                    xtype: 'button',
                                    flex: 1,
                                    dock: 'top',
                                    margin: 10,
                                    text: 'חפש',
                                    listeners: {
                                        click: {
                                            fn: me.onButtonClick,
                                            scope: me
                                        }
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            collapsed: true,
                            title: 'My Panel'
                        },
                        {
                            xtype: 'panel',
                            collapsed: true,
                            title: 'My Panel'
                        }
                    ]
                },
                {
                    xtype: 'tabpanel',
                    region: 'center',
                    id: 'MainTabPanelID',
                    activeTab: 0,
                    items: [
                        {
                            xtype: 'panel',
                            layout: {
                                type: 'border'
                            },
                            title: 'General',
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    items: [
                                        {
                                            xtype: 'button',
                                            id: 'btnRefreshId',
                                            icon: 'images/refresh16x16.png',
                                            text: 'רענון',
                                            listeners: {
                                                click: {
                                                    fn: me.onBtnRefreshIdClick,
                                                    scope: me
                                                }
                                            }
                                        }
                                    ]
                                }
                            ],
                            items: [
                                {
                                    xtype: 'container',
                                    region: 'center',
                                    layout: {
                                        align: 'stretch',
                                        type: 'vbox'
                                    },
                                    items: [
                                        {
                                            xtype: 'gridpanel',
                                            flex: 1,
                                            autoScroll: true,
                                            title: 'שיחות נכנסות',
                                            store: 'IncomingDataStore',
                                            viewConfig: {
                                                id: 'IncomingGridId'
                                            },
                                            columns: [
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'trunk_owner',
                                                    text: 'Owner'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'country_name',
                                                    text: 'Country'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'country_desc',
                                                    text: 'Description'
                                                },
                                                {
                                                    xtype: 'numbercolumn',
                                                    align: 'right',
                                                    dataIndex: 'call_duration',
                                                    text: 'Duration Hourly',
                                                    format: '0,000'
                                                },
                                                {
                                                    xtype: 'numbercolumn',
                                                    align: 'right',
                                                    dataIndex: 'acd',
                                                    text: 'ACD Hourly'
                                                },
                                                {
                                                    xtype: 'numbercolumn',
                                                    align: 'right',
                                                    dataIndex: 'asr',
                                                    text: 'ASR Hourly'
                                                },
                                                {
                                                    xtype: 'numbercolumn',
                                                    align: 'right',
                                                    dataIndex: 'calls',
                                                    text: 'Calls Hourly',
                                                    format: '0,000'
                                                }
                                            ],
                                            listeners: {
                                                celldblclick: {
                                                    fn: me.onGridpanelCellDblClick1,
                                                    scope: me
                                                }
                                            }
                                        },
                                        {
                                            xtype: 'gridpanel',
                                            flex: 2,
                                            autoScroll: true,
                                            title: 'שיחות יוצאת',
                                            store: 'OutgoingDataStore',
                                            viewConfig: {
                                                getRowClass: function(record, rowIndex, rowParams, store) {
                                                    var acd_h = record.get('acd_h');
                                                    var acd_m = record.get('acd_m');

                                                    console.log('Cheking!!! ACD_H:' + acd_h + ' < ACD_M:' + acd_m);

                                                    if (acd_h < acd_m - 1){
                                                        return 'price-fall';
                                                    }

                                                },
                                                draggable: false,
                                                id: 'OutGoingGridId'
                                            },
                                            columns: [
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'country_name',
                                                    text: 'Country'
                                                },
                                                {
                                                    xtype: 'numbercolumn',
                                                    width: 50,
                                                    align: 'right',
                                                    dataIndex: 'country_code',
                                                    text: 'Code',
                                                    format: '0,000'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'country_desc',
                                                    text: 'Description'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    align: 'right',
                                                    dataIndex: 'call_duration_h',
                                                    text: 'Duration Hourly'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    align: 'right',
                                                    dataIndex: 'call_duration_m',
                                                    text: 'Duration Monthly'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    hidden: true,
                                                    dataIndex: 'priority',
                                                    text: 'Priority'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    align: 'right',
                                                    dataIndex: 'acd_h',
                                                    text: 'ACD Hourly'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    align: 'right',
                                                    dataIndex: 'acd_m',
                                                    text: 'ACD Monthly'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    align: 'right',
                                                    dataIndex: 'asr_h',
                                                    text: 'ASR Hourly'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    align: 'right',
                                                    dataIndex: 'asr_m',
                                                    text: 'ASR Monthly'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    align: 'right',
                                                    dataIndex: 'calls_h',
                                                    text: 'Calls hourly'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    align: 'right',
                                                    dataIndex: 'calls_m',
                                                    text: 'Calls Monthly'
                                                }
                                            ],
                                            listeners: {
                                                celldblclick: {
                                                    fn: me.onGridpanelCellDblClick,
                                                    scope: me
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    },

    onButtonClick: function(button, e, options) {
        //Ext.MsgBox(Ext.getCmp('txtPrefixID').value);
        var val = Ext.getCmp('txtPrefixID').value;
        var v_url = 'GetCountryPrefixes.jsp?' + Ext.urlEncode({'prefix': val} );
        // {'prefix':'66', 'vendor':'1GLOBAL', 'rate':'.0065', 'call_duration':'18645620', 'call_cound':'51265' },
        // Ext.Msg.alert('Status', 'URL: [' + url + ']');
        // {'trunk_owner':'NEXT', 'prefix':'668116', 'rate':'.0184', 'currency':'USD', 'acd':'3.26', 'asr':'41' },  
        var newTab = Ext.create('Ext.panel.Panel', {
            title: 'Price Usage Report',
            autoScroll: true,
            layout: {
                type: 'fit'
            },
            closable: true,
            items:  [{
                xtype: 'gridpanel',
                autoShow: false,
                autoScroll: true,
                store:  Ext.create('Ext.data.Store', {
                    fields: [
                    {name: 'trunk_owner'},
                    {name: 'prefix', type: 'int', sortType: 'asInt'},
                    {name: 'rate', type: 'float', sortType: 'asFloat'},
                    {name: 'currency'},
                    {name: 'acd', type: 'int', sortType: 'asInt'},
                    {name: 'asr', type: 'int', sortType: 'asInt'}
                    ],
                    proxy: {
                        type: 'ajax',
                        timeout: 120000,
                        url: v_url,
                        reader: {
                            type: 'json',
                            root: 'data',
                            successProperty: 'success'
                        }
                    },
                    autoLoad: true
                }),
                title: 'Price Reprort for "' + val + '" - Monthly' ,
                columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'prefix',
                    width: 80,
                    text: 'Prefix'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'trunk_owner',
                    width: 150,
                    text: 'Vendor'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'rate',
                    width: 50,
                    text: 'Rate'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'currency',
                    width: 100,
                    text: 'Currency'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'acd',
                    width: 100,
                    text: 'ACD'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'asr',
                    width: 100,
                    text: 'ASR'
                }]
            }]
        });

        var panel = Ext.getCmp("MainTabPanelID");
        panel.add(newTab).show();

    },

    onBtnRefreshIdClick: function(button, e, options) {
        var grid = Ext.getCmp('OutGoingGridId');
        grid.getStore().reload();
        var grid2 = Ext.getCmp('IncomingGridId');
        grid2.getStore().reload();
        // IncomingGridId
    },

    onGridpanelCellDblClick1: function(tablepanel, td, cellIndex, record, tr, rowIndex, e, options) {
        var title =  record.getData().country_name + '(' + record.getData().country_desc + ') " - Today';
        var v_url = 'GetIncomingDetails.jsp?' + Ext.urlEncode({'country': record.getData().country_name, 'desc' : record.getData().country_desc} );
        var newTab = Ext.create('Ext.panel.Panel', {
            title: title,
            autoScroll: true,
            layout: {
                type: 'fit'
            },
            closable: true,
            items:  [{
                xtype: 'gridpanel',
                autoShow: false,
                autoScroll: true,
                store:  Ext.create('Ext.data.Store', {
                    fields: [
                    {name: 'call_date'},
                    {name: 'owner_in'},
                    {name: 'trunk_in'},           
                    {name: 'owner_out'},
                    {name: 'trunk_out'},
                    {name: 'acd', type: 'float'},
                    {name: 'asr', type: 'float'},
                    {name: 'calls', type: 'int'}
                    ],
                    proxy: {
                        type: 'ajax',
                        timeout: 120000,
                        url: v_url,
                        reader: {
                            type: 'json',
                            root: 'data',
                            successProperty: 'success'
                        }
                    },
                    autoLoad: true
                }),
                title: 'Incoming information for "' + record.getData().country_name + '" - Today' ,
                /*
                viewConfig: {

                getRowClass: function(record, rowIndex, rowParams, store) {
                var acd_h = record.get('acd_h');
                var acd_m = record.get('acd_m');
                if (acd_h < acd_m - 1){
                return 'price-fall';
                }
                }
                },

                call_date
                owner_in
                trunk_in
                owner_out
                trunk_out
                acd
                asr
                calls

                */
                columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'call_date',
                    width: 150,
                    text: 'Call Date'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'owner_in',
                    width: 150,
                    text: 'Owner IN'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'trunk_in',
                    align: 'right',
                    width: 200,
                    text: 'Trunk in'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'owner_out',
                    width: 150,
                    text: 'Owner OUT'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'trunk_out',
                    align: 'right',
                    width: 200,
                    text: 'Trunk OUT'
                },{
                    xtype: 'gridcolumn',
                    align: 'right',
                    dataIndex: 'acd',
                    width: 50,
                    text: 'ACD'
                },{
                    xtype: 'gridcolumn',
                    align: 'right',
                    dataIndex: 'asr',
                    width: 50,
                    text: 'ASR'
                },{
                    xtype: 'gridcolumn',
                    align: 'right',
                    dataIndex: 'calls',
                    width: 50,
                    text: 'Calls'
                }]
            }]
        });

        var panel = Ext.getCmp("MainTabPanelID");
        panel.add(newTab).show();

    },

    onGridpanelCellDblClick: function(tablepanel, td, cellIndex, record, tr, rowIndex, e, options) {
        var title =  record.getData().country_name + '(' + record.getData().country_desc + ') " - Today';
        var v_url = 'GetOutgoingDetails.jsp?' + Ext.urlEncode({'country': record.getData().country_name, 'desc' : record.getData().country_desc} );
        var newTab = Ext.create('Ext.panel.Panel', {
            title: title,
            autoScroll: true,
            layout: {
                type: 'fit'
            },
            closable: true,
            items:  [{
                xtype: 'gridpanel',
                autoShow: false,
                autoScroll: true,
                store:  Ext.create('Ext.data.Store', {
                    fields: [
                    {name: 'call_date'},
                    {name: 'trunk_owner'},
                    {name: 'trunk_out'},
                    {name: 'acd_h', type: 'float'},
                    {name: 'acd_m', type: 'float'},
                    {name: 'asr_h', type: 'int'},
                    {name: 'asr_m', type: 'int'},
                    {name: 'mins_h', type: 'int'},
                    {name: 'mins_m', type: 'int'}
                    ],
                    proxy: {
                        type: 'ajax',
                        timeout: 120000,
                        url: v_url,
                        reader: {
                            type: 'json',
                            root: 'data',
                            successProperty: 'success'
                        }
                    },
                    autoLoad: true
                }),
                title: 'General Data for "' + record.getData().country_name + '" - Today' ,
                viewConfig: {
                    getRowClass: function(record, rowIndex, rowParams, store) {
                        var acd_h = record.get('acd_h');
                        var acd_m = record.get('acd_m');
                        if (acd_h < acd_m - 1){
                            return 'price-fall';
                        }
                    }
                },
                columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'call_date',
                    width: 150,
                    text: 'Hour'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'trunk_owner',
                    width: 150,
                    text: 'Trunk Owner'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'trunk_out',
                    width: 200,
                    text: 'Trunk Out'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'acd_h',
                    width: 50,
                    text: 'ACD Hourly'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'acd_m',
                    width: 50,
                    text: 'ACD Monthly'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'asr_h',
                    width: 50,
                    text: 'ASR Hourly'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'asr_m',
                    width: 50,
                    text: 'ASR Monthly'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'mins_h',
                    width: 50,
                    text: 'Minutes H'
                },{
                    xtype: 'gridcolumn',
                    dataIndex: 'mins_m',
                    width: 50,
                    text: 'Minutes M'
                }]
            }]
        });

        var panel = Ext.getCmp("MainTabPanelID");
        panel.add(newTab).show();

    }

});