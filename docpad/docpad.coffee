# DocPad Configuration File
# http://docpad.org/docs/config

# Define the DocPad Configuration
docpadConfig = {
	# Root Path
    # The root path of our our project
    rootPath: process.cwd()
	
	# Out Path
    # Where should we put our generated website files?
    # If it is a relative path, it will have the resolved `rootPath` prepended to it
    outPath: 'out/../../'

    # Src Path
    # Where can we find our source website files?
    # If it is a relative path, it will have the resolved `rootPath` prepended to it
    srcPath: 'src'

    # Documents Paths
    # An array of paths which contents will be treated as documents
    # If it is a relative path, it will have the resolved `srcPath` prepended to it
    documentsPaths: [
        'documents'
    ]

    # Files Paths
    # An array of paths which contents will be treated as files
    # If it is a relative path, it will have the resolved `srcPath` prepended to it
    filesPaths: [
        'files'
        'public'
    ]

    # Layouts Paths
    # An array of paths which contents will be treated as layouts
    # If it is a relative path, it will have the resolved `srcPath` prepended to it
    layoutsPaths: [
        'layouts'
    ]
	
	# Port
    # Use to change the port that DocPad listens to
    # By default we will detect the appropriate port number for our environment
    # if no environment port number is detected we will use 9778 as the port number
    # Checked environment variables are:
    # - PORT - Heroku, Nodejitsu, Custom
    # - VCAP_APP_PORT - AppFog
    # - VMC_APP_PORT - CloudFoundry
    port: 80
	
	# =================================
    # Template Configuration

    # Template Data
    # Use to define your own template data and helpers that will be accessible to your templates
    # Complete listing of default values can be found here: http://docpad.org/docs/template-data
    templateData:
        site:
            url: "http://obullxl.github.io"
            title: "OSN开源云-做最好的Java,Node.js,Bootstrap,Git,GitHub技术交流云博客"
            description: """
                Java编程,Node.js Bootstrap Semantic-UI Node Spring SpringMVC Struts1 Struts2 Webwork框架深入,Freemarker Velocity模板使用,XMLHTTP Ajax开发,Java Web开发,Java企业应用,Java设计模式,Java开源框架,Java应用服务器,Rich Client讨论,JavaScript编程
                """
            keywords: """
                Java, Node.js, Bootstrap, Semantic-UI, GitHub, Git, JavaScript, Spring, SpringMVC, Ibatis, MyBatis
                """

        getPreparedTitle: ->
            if @document.title
                "#{@document.title} | #{@site.title}"
            else
                @site.title

        getPreparedDescription: ->
            @document.description or @site.description

        getPreparedKeywords: ->
            @site.keywords.concat(@document.keywords or []).join(', ')
}

# Export the DocPad Configuration
module.exports = docpadConfig
