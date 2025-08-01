cl{{- define "exchange-generator-service.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "exchange-generator-service.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}

{{- define "exchange-generator-service.selectorLabels" -}}
app: {{ include "blocker-service.name" . }}
{{- end }}

{{- define "exchange-generator-service.labels" -}}
app: {{ include "exchange-generator-service.name" . }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
release: {{ .Release.Name }}
heritage: {{ .Release.Service }}
{{- end }}